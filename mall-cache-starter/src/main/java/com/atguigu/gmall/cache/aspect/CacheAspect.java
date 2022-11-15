package com.atguigu.gmall.cache.aspect;

import com.atguigu.gmall.cache.annotation.MallCache;
import com.atguigu.gmall.cache.service.CacheService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.item.aspect
 * @ClassName : CacheAspect.java
 * @createTime : 2022/11/15 11:42
 * @Description :
 */

@EnableAspectJAutoProxy //开启基于注解的切面功能
@Component
@Aspect
public class CacheAspect {
    @Autowired
    CacheService cacheService;

    //表达式解析器
    SpelExpressionParser parser = new SpelExpressionParser();

    @Autowired
    RedissonClient redissonClient;

    @Pointcut("@annotation(com.atguigu.gmall.cache.annotation.MallCache)")
    public void pc(){};

    /**
     * 在任意方法都能标注注解切入进去
     * @param pjp 连接点（封装上下文【目标方法的详细信息】）
     * @return
     */
    @Around("pc()")
    public Object cacheAround(ProceedingJoinPoint pjp){
        //获取传来的参数
        Object[] args = pjp.getArgs();
        //锁状态
        boolean lockStatus = false;
        RLock lock = null;
        Object retVal = null;
        try {
            //1、先查缓存; 【商品详情：sku:info:xxx  分类： category  订单：order:info:xxx】
            //1）怎么根据方法不同就用不同的缓存key
            //========动态计算缓存key==========
            String cacheKey = determineCacheKey(pjp);

            //2）不同方法由于返回值不同，所以从缓存中拿到的数据不同。怎么拿到不同数据；
            //拿到缓存中的数据转为精确类型  fastjson：出现不预期的结果   jackson：不会出现错误结果
            //========动态逆转json数据==========
            Object cacheData = getCacheDataObj(pjp,cacheKey);

            //2、缓存没有
            if (cacheData == null){
                //是否启用bitmap
                boolean b = true;
                //3、回源之前： bitmap过滤   【防止随机值穿透攻击】
                // 3) bitmap不同业务用的也不一样；（允许各种场景的BitMap）、bitmap不一定用
                MallCache annotation = getAnnotation(pjp, MallCache.class);
                String bitmapKeyExpr = annotation.bitmapKey();
                String bitmapIndexExpr = annotation.bitmapIndex();

                //=========动态启用bitmap===========
                if(StringUtils.isEmpty(bitmapKeyExpr)){
                    //不启用Bitmap
                    b = false;
                }else {
                    //启用bitmap
                    String bmIndex = evalutionValue(bitmapIndexExpr, pjp);
                    String bmKey = evalutionValue(bitmapKeyExpr, pjp);
                    //判断指定bitmap中是否有这个索引位置
                    b = cacheService.existFromBitMap(bmKey,Long.parseLong(bmIndex));
                }
                if (b){
                    //4、开始回源           【防止缓存击穿】 lock:sku:info:50
                    //TODO 4）不同业务锁也不一样
                    lock = redissonClient.getLock("lock:" + cacheKey);
                    //5、加分布式锁
                    lockStatus = lock.tryLock();
                    if (lockStatus){
                        //7、加锁成功
                        retVal = pjp.proceed(); //执行目标方法。【回源完成】
                        //8、放入缓存
                        //==========动态指定数据的过期时间============
                        //TODO 5）数据给缓存中放的时候有可能要根据不同业务指定不一样的过期时间
                        long ttl = annotation.ttl();
                        TimeUnit unit = annotation.unit();
                        cacheService.saveCacheData(cacheKey,retVal,ttl,unit);
                        //9、返回最终的值
                        return retVal;
                    }else {
                        //6、加锁失败
                        Thread.sleep(300);
                        return getCacheDataObj(pjp,cacheKey);
                    }
                }else {
                    return null;
                }
            }else {
                return cacheData;
            }
        } catch (Throwable e) {
            //异常通知
            e.printStackTrace();//收到异常一定继续往出抛
            throw new RuntimeException(e);
        }finally {
            //后置通知； 解锁
            if (lockStatus){
                lock.unlock();
            }
        }
    }

    /**
     * 从缓存中获取数据，并逆转为 目标方法的返回值类型对象
     * @param pjp
     * @param cacheKey
     * @return
     */
    private Object getCacheDataObj(ProceedingJoinPoint pjp, String cacheKey) {
        return cacheService.getCacheDataObj(cacheKey, new TypeReference<Object>(){
            @Override
            public Type getType(){
                return getMethodReturnType(pjp);
            }
        });
    }

    private Type getMethodReturnType(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Type type = method.getGenericReturnType();
        return type;
    }

    /**
     * 决定缓存用哪个key。
     * @param pjp 连接点
     * @return 返回一个Key；
     */
    private String determineCacheKey(ProceedingJoinPoint pjp) {
        MallCache annotation = getAnnotation(pjp,MallCache.class);
        //4、拿到cacheKey
        String cacheKey = annotation.cacheKey();

        //sku:info:#{#args[0]}：
        //自己编写表达式计算逻辑
        // 1、字符串的截串操作，完成表达式计算
        // 2、${ 截串的起始位置， } 结束位置； args[0]
        // 3、截串 [ ] 里面的值是 0 ，代表 取出 args的数组索引为0的值
        //根据一个表达式计算出这个表达式真正应该是的值

        String str = evalutionValue(cacheKey,pjp);
        //5、缓存整个完整使用的键；
        return str;
    }

    /**
     * 计算一个表达式的值
     * @param cacheKey
     * @param pjp
     * @return
     */
    private String evalutionValue(String cacheKey, ProceedingJoinPoint pjp) {
        //1、解析表达式
        Expression expression = parser.parseExpression(cacheKey, ParserContext.TEMPLATE_EXPRESSION);

        StandardEvaluationContext context = new StandardEvaluationContext();
        //给上下文准备值
        Object[] args = pjp.getArgs();
        context.setVariable("args",args);
        context.setVariable("currentDate",new Date());

        //2、计算值
        String value = expression.getValue(context, String.class);
        return value;
    }

    /**
     * 获取目标方法中标注的指定注解
     * @param pjp
     * @param annotation
     * @param <T>
     * @return
     */
    private <T extends Annotation> T getAnnotation(ProceedingJoinPoint pjp, Class<T> annotation){
        //1,获取到当前方法的完整签名
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        //2.拿到当前方法
        Method method = signature.getMethod();
        //3、拿到方法指定注解
        T declaredAnnotations = method.getDeclaredAnnotation(annotation);
        return declaredAnnotations;
    }
}