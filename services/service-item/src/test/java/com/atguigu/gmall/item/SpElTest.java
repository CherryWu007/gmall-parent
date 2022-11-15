package com.atguigu.gmall.item;

import com.atguigu.gmall.item.vo.SkuDetailVo;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.junit.jupiter.api.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName SpElTest
 * @Description 此类描述:字符串动态表达式测试类
 * @Author dangchen
 * @DateTime 2022-11-08 19:48
 * @Version 1.0
 */
public class SpElTest {

    class helloWorld{
        public List<Map<String, SkuDetailVo>> getHaha(){
            return null;
        }

        public SkuDetailVo getHehe(){
            return null;
        }
    }

    @Test
    void test04(){
        for (Method method:helloWorld.class.getDeclaredMethods()){
            System.out.println(method.getName() +"==>getReturnType==>" + method.getReturnType());//复杂返回值得出的返回类型太粗糙
            System.out.println(method.getName() +"==>getGenericReturnType==>" + method.getGenericReturnType());
            System.out.println("----------");
        }
    }

    @Test
    void test03(){
        SpelExpressionParser parser = new SpelExpressionParser();
        //String expr = "sku:info:#{args[0]}";// args前一个 # 号,识别为函数自己的变量了
        String expr = "sku:info:#{#args[0]}";

        Expression expression = parser.parseExpression(expr, ParserContext.TEMPLATE_EXPRESSION);
        //有提前准备好的一些值，方便接下来计算表达式的时候获取
        //由于表达式内存有可能要取值
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("args", Arrays.asList(13,35,75));

        Object value = expression.getValue(context);
        //计算上下文
        System.out.println(value);
    }

    @Test
    void test02(){
        //SpelExpressionParser:表达式解析器 ↓ 传入
        //parseExpression表达式-->通过解析计算-->Expression ↓
        //expression.getValue()获得表达式的真正值
        SpelExpressionParser parser = new SpelExpressionParser();

        //String expr = "sku:info:#{new String('abc').toUpperCase()}";

        String expr = "sku:info:#{T(java.util.UUID).randomUUID().toString().substring(0,5).toUpperCase()}";
        Expression expression = parser.parseExpression(expr, ParserContext.TEMPLATE_EXPRESSION);
        Object value = expression.getValue();
        System.out.println(value);
    }

    @Test
    void test01(){
        //1.创建表达式解析器
        SpelExpressionParser parser = new SpelExpressionParser();

        //2.使用解析器解析一个表达式
        //String expr = "10/2 + (3+1)";//9
        //String expr = "new String('dangchen').toUpperCase()"; //DANGCHEN
        //String expr = "'helloworld' + 3"; //helloworld3
        String expr = "'hello'.concat('world')";//helloworld
        Expression expression = parser.parseExpression(expr);

        //3.得到表达式的值
        Object value = expression.getValue();
        System.out.println(value + "==>" + value.getClass());
    }
}
