package com.atguigu.gmall.search.service.impl;

import com.atguigu.gmall.common.constant.RedisConst;
import com.google.common.collect.Lists;
import com.atguigu.gmall.search.vo.SearchRespVo.OrderMapVo;

import com.atguigu.gmall.search.entity.Goods;
import com.atguigu.gmall.search.repository.GoodsRepository;
import com.atguigu.gmall.search.service.SearchService;
import com.atguigu.gmall.search.vo.SearchParamVo;
import com.atguigu.gmall.search.vo.SearchRespVo;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.mapper.ParseContext;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.*;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 老贼
 * @version : 1.0
 * @Package : com.atguigu.gmall.search.service.impl
 * @ClassName : SearchServiceImpl.java
 * @createTime : 2022/11/17 15:51
 * @Description :
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    GoodsRepository goodsRepository;

    @Autowired
    ElasticsearchRestTemplate esTemplate;

    @Override
    public SearchRespVo search(SearchParamVo vo) {
        //TODO 给ES发送检索的完整DSL
        //检索条件
        Query query = buildSearchDSL(vo);
        //搜索
        SearchHits<Goods> hits = esTemplate.search(query, Goods.class, IndexCoordinates.of("goods"));
        //3、处理结果
        SearchRespVo result = buildSearchResp(hits, vo);

        return result;
    }

    /**
     * 根据es检索到的结果，构建前端需要的返回数据
     *
     * @param result
     * @return
     */
    private SearchRespVo buildSearchResp(SearchHits<Goods> result, SearchParamVo vo) {
        //准备封装前端需要得返回值
        SearchRespVo respVo = new SearchRespVo();
        //1、封装前端传过来得所有得检索条件
        respVo.setSearchParam(vo);
        //============面包屑===============
        //2、封装品牌面包屑   品牌：小米
        if (!StringUtils.isEmpty(vo.getTrademark())) {
            String trademarkName = vo.getTrademark().split(":")[1];
            respVo.setTrademarkParam("品牌" + trademarkName);
        }
        //3、属性面包屑
       /* List<SearchRespVo.AttrVo> props=new ArrayList<>();
        if (vo.getProps()!=null&&vo.getProps().length>0) {
            for (String prop : vo.getProps()) {
                String[] split = prop.split(":");
                SearchRespVo.AttrVo attrVo = new SearchRespVo.AttrVo();
                attrVo.setAttrId(Long.parseLong(split[0]));
                attrVo.setAttrName(split[2]);
                attrVo.setAttrValue(split[1]);
                props.add(attrVo);
            }
        }*/
        //使用stream流式精简代码
        List<SearchRespVo.AttrVo> collect = Arrays.stream(vo.getProps())
                .map(item -> {//流中得旧元素
            String[] split = item.split(":");
            SearchRespVo.AttrVo attrVo = new SearchRespVo.AttrVo();
            attrVo.setAttrId(Long.parseLong(split[0]));
            attrVo.setAttrName(split[2]);
            attrVo.setAttrValue(split[1]);
            return attrVo;//返回得新元素
        }).collect(Collectors.toList());


        respVo.setPropsParamList(collect);
//==================面包屑完成================
        //二次检索区：品牌列表
        List<SearchRespVo.trademarkVo> trademarkList = new ArrayList<>();
        ParsedLongTerms tmIdAgg = result.getAggregations().get("tmIdAgg");
        //聚合的返回值类型，起名规则: Parsed 聚合用的属性类型 聚合类型
        for (Terms.Bucket bucket : tmIdAgg.getBuckets()) {
            //品牌id
            long attrId = bucket.getKeyAsNumber().longValue();
            //品牌name
            ParsedStringTerms tmNameAgg = bucket.getAggregations().get("tmNameAgg");
            String tmName = tmNameAgg.getBuckets().get(0).getKeyAsString();
            SearchRespVo.trademarkVo trademark = new SearchRespVo.trademarkVo();
            //品牌logo
            ParsedStringTerms tmLogoAgg = bucket.getAggregations().get("tmLogoAgg");
            String tmLogo = tmLogoAgg.getBuckets().get(0).getKeyAsString();

            trademark.setTmId(attrId);
            trademark.setTmName(tmName);
            trademark.setTmLogoUrl(tmLogo);
            trademarkList.add(trademark);
        }
        //品牌列表填充完成
        respVo.setTrademarkList(trademarkList);

        //二次检索区：平台属性列表
        List<SearchRespVo.baseAttrInfo> attrInfos = new ArrayList<>();
        ParsedNested attrAgg = result.getAggregations().get("attrAgg");
        //根据attrId分组第一个聚合查询获取Id值
        ParsedLongTerms attrIdAgg = attrAgg.getAggregations().get("attrIdAgg");
        for (Terms.Bucket bucket : attrIdAgg.getBuckets()) {
            SearchRespVo.baseAttrInfo baseAttrInfo = new SearchRespVo.baseAttrInfo();
            //属性Id
            long attrId = bucket.getKeyAsNumber().longValue();
            baseAttrInfo.setAttrId(attrId);
            //属性名
            String attrName = ((ParsedStringTerms) bucket.getAggregations().get("attNameAgg"))
                    .getBuckets().get(0).getKeyAsString();
            baseAttrInfo.setAttrName(attrName);
            //属性值 集合
            List<String> attrValueList = new ArrayList<>();
            List<? extends Terms.Bucket> attrValueAgg = ((ParsedStringTerms) bucket.getAggregations().get("attrValueAgg")).getBuckets();
            for (Terms.Bucket item : attrValueAgg) {
                String keyAsString = item.getKeyAsString();
                attrValueList.add(keyAsString);
            }

            baseAttrInfo.setAttrValueList(attrValueList);
            attrInfos.add(baseAttrInfo);

        }
        respVo.setAttrsList(attrInfos);

        //4、把所有检索到得Goods集合拿到
        ArrayList<Goods> goodsList = new ArrayList<>();
        for (SearchHit<Goods> searchHit : result.getSearchHits()) {
            //查到得商品
            Goods goods = searchHit.getContent();
            if (!StringUtils.isEmpty(vo.getKeyword())) {
                //高亮显示匹配字段
                String title = searchHit.getHighlightField("title").get(0);
                goods.setTitle(title);
            }
            goodsList.add(goods);
        }
        respVo.setGoodsList(goodsList);


        //5、order排序,必须返回
        OrderMapVo orderMapVo = new OrderMapVo();
        if (!StringUtils.isEmpty(vo.getOrder())) {
            String[] split = vo.getOrder().split(":");
            orderMapVo.setSort(split[1]);
            orderMapVo.setType(split[0]);
        }
        respVo.setOrderMap(orderMapVo);
        //6、封装页码数据

        respVo.setPageNo(vo.getPageNo());
        //总页码配置
        long totalHits = result.getTotalHits();
        Long pageLimit = totalHits % RedisConst.PAGE_SIZE == 0 ? totalHits / RedisConst.PAGE_SIZE : (totalHits / RedisConst.PAGE_SIZE + 1);
        respVo.setTotalPages(pageLimit);
        //url连接上得数据
        String url = convertRequestParamToUrl(vo);
        respVo.setUrlParam(url);

        return respVo;
    }

    //封装url
    private String convertRequestParamToUrl(SearchParamVo vo) {
        StringBuilder stringBuilder = new StringBuilder("/list.html?");
        Long category1Id = vo.getCategory1Id();
        if (category1Id != null) {
            stringBuilder.append("&category1Id=").append(category1Id);
        }
        Long category2Id = vo.getCategory2Id();
        if (category2Id != null) {
            stringBuilder.append("&category2Id=").append(category2Id);
        }
        Long category3Id = vo.getCategory3Id();
        if (category3Id != null) {
            stringBuilder.append("&category3Id=").append(category3Id);
        }
        String keyword = vo.getKeyword();
        if (!StringUtils.isEmpty(keyword)) {
            stringBuilder.append("&keyword=").append(keyword);
        }
        String trademark = vo.getTrademark();
        if (!StringUtils.isEmpty(trademark)) {
            stringBuilder.append("&trademark=").append(trademark);
        }
        String[] props = vo.getProps();
        if (props != null && props.length > 0) {
            for (String prop : props) {
                stringBuilder.append("&props=").append(prop);
            }
        }

        return stringBuilder.toString();
    }

    /**
     * 跟库前端传递的检索条件，构建es用的DSL
     *
     * @param vo 前端的条件
     * @return
     */
    private Query buildSearchDSL(SearchParamVo vo) {
        //=============1、构建查询条件=================
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //根据前端传递夺来的参数情况，动态构建DSL
        //==============分类条件=====================
        if (vo.getCategory3Id() != null) {
            TermQueryBuilder category3IdQuery = QueryBuilders.termQuery("category3Id", vo.getCategory3Id());
            boolQuery.must(category3IdQuery);
        }
        if (vo.getCategory2Id() != null) {
            TermQueryBuilder category2IdQuery = QueryBuilders.termQuery("category2Id", vo.getCategory2Id());
            boolQuery.must(category2IdQuery);
        }

        if (vo.getCategory1Id() != null) {
            TermQueryBuilder category1IdQuery = QueryBuilders.termQuery("category1Id", vo.getCategory1Id());
            boolQuery.must(category1IdQuery);
        }
        //==============模糊检索（商品名）===========
        if (!StringUtils.isEmpty(vo.getKeyword())) {
            MatchQueryBuilder title = QueryBuilders.matchQuery("title", vo.getKeyword());
            boolQuery.must(title);
        }

        //================品牌检索====================
        if (!StringUtils.isEmpty(vo.getTrademark())) {
            //提取品牌id进行精确term
            long tmId = Long.parseLong(vo.getTrademark().split(":")[0]);
            boolQuery.must(QueryBuilders.termQuery("tmId", tmId));
        }
        //=============属性检索==============
        //props=4:256GB:机身存储&props=3:8GB:运行内存
        if (vo.getProps() != null && vo.getProps().length > 0) {
            for (String prop : vo.getProps()) {
                //4:256GB:机身存储
                String[] split = prop.split(":");
                long attrId = Long.parseLong(split[0]);
                String attrValue = split[1];
                //查询条件
                BoolQueryBuilder bool = QueryBuilders.boolQuery();
                bool.must(QueryBuilders.termQuery("attrs.attrId", attrId));
                bool.must(QueryBuilders.termQuery("attrs.attrValue", attrValue));

                //嵌入式
                NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("attrs", bool, ScoreMode.None);
                boolQuery.must(nestedQuery);

            }
        }
        //========以上检索条件完成===========
        //以下做分页排序
        //原生DSL
        NativeSearchQuery dsl = new NativeSearchQuery(boolQuery);
        //============排序=============
        if (!StringUtils.isEmpty(vo.getOrder())) {
            String[] split = vo.getOrder().split(":");
            String type = split[0];//排序类型
            String order = split[1];//顺序

            String orderField = "";
            switch (type) {
                case "1":
                    orderField = "hotScore";
                    break;
                case "2":
                    orderField = "price";
                    break;
                default:
                    orderField = "hotScore";
            }
            //根据前端参数得到排序条件
            Sort.Direction direction = Sort.Direction.ASC;
            try {
                direction = Sort.Direction.fromString(order);
            } catch (Exception ignored) {
            }
            Sort sort = Sort.by(direction, orderField);
            dsl.addSort(sort);

        }
        //===============分页条件
        if (vo.getPageNo() != null && vo.getPageNo() > 0) {
            Pageable pageable = PageRequest.of(vo.getPageNo() - 1, 10);

            dsl.setPageable(pageable);
        }
        //===============高亮条件
        if (!StringUtils.isEmpty(vo.getKeyword())) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("title").preTags("<span style='color:red'>").postTags("</span>");
            dsl.setHighlightQuery(new HighlightQuery(highlightBuilder));
        }


        //聚合分析============
        //ds.addAggregation(); //添加分析条件
        //品牌聚合分析
        TermsAggregationBuilder tmIdAgg = AggregationBuilders.terms("tmIdAgg")
                .field("tmId")
                .size(100);
        TermsAggregationBuilder tmNameAgg = AggregationBuilders.terms("tmNameAgg")
                .field("tmName").
                size(1);
        tmIdAgg.subAggregation(tmNameAgg);
        TermsAggregationBuilder tmLogoAgg = AggregationBuilders.terms("tmLogoAgg").field("tmLogoUrl").size(1);
        tmIdAgg.subAggregation(tmLogoAgg);

        dsl.addAggregation(tmIdAgg); //添加分析条件

        //=======属性聚合分析======
        NestedAggregationBuilder attrAgg = AggregationBuilders.nested("attrAgg", "attrs");
        //attrId聚合
        TermsAggregationBuilder attrIdAgg = AggregationBuilders.terms("attrIdAgg").field("attrs.attrId").size(100);
        //attrName聚合
        TermsAggregationBuilder attrNameAgg = AggregationBuilders.terms("attrNameAgg").field("attrs.attrName").size(1);
        attrIdAgg.subAggregation(attrNameAgg);
        //attrValue聚合
        TermsAggregationBuilder attrValueAgg = AggregationBuilders.terms("attrValueAgg").field("attrs.attrValue").size(100);
        attrIdAgg.subAggregation(attrValueAgg);

        attrAgg.subAggregation(attrIdAgg);
        dsl.addAggregation(attrAgg);

        return dsl;
    }

    @Override
    public void saveGoods(Goods goods) {
        goodsRepository.save(goods);
    }

    @Override
    public void deleteGoods(Long skuId) {
        goodsRepository.deleteById(skuId);
    }

    @Autowired
    ElasticsearchRestTemplate template;

    @Override
    public void updateScore(Long skuId, Long increment) {
//        Goods goods = goodsRepository.findById(skuId).get();
//        goods.setHotScore(increment);
//        goodsRepository.save(goods);


        //部分字段更新
        Document document = Document.create();
        document.append("hotScore", increment);

        UpdateQuery query = UpdateQuery.builder("" + skuId).withDocAsUpsert(true)
                .withDocument(document).build();
        template.update(query, IndexCoordinates.of("goods"));
    }
}
