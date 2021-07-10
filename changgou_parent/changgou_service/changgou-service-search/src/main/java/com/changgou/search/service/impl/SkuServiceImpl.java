package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.entity.Result;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.search.dao.SkuEsMapper;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SearchResultMapperImpl;
import com.changgou.search.service.SkuService;
import jdk.nashorn.internal.parser.JSONParser;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private SkuEsMapper skuEsMapper;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public void importSku() {

        Result<List<Sku>> res=skuFeign.findByStatus("1");

        List<SkuInfo> list= JSON.parseArray(JSON.toJSONString(res.getData()),SkuInfo.class);
        for (SkuInfo skuInfo:list)
        {
            Map<String,Object> map= JSON.parseObject(skuInfo.getSpec());
            skuInfo.setSpecMap(map);
        }
        skuEsMapper.saveAll(list);


    }



    @Override
    public Map search(Map<String, String> searchMap) {

        String keywords=searchMap.get("keywords");
        if (StringUtils.isEmpty(keywords))
            keywords="华为";

       //1.创建查询对象 的构建对象
        NativeSearchQueryBuilder nativeSearchQueryBuilder=new NativeSearchQueryBuilder();
        //2.构造查询
        //a.bool查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
         //设置bool查询条件
        if (!StringUtils.isEmpty(searchMap.get("brand")))
        {
            boolQueryBuilder.filter(QueryBuilders.termQuery("brandName",searchMap.get("brand")));
        }
        if (!StringUtils.isEmpty(searchMap.get("category")))
        {
            boolQueryBuilder.filter(QueryBuilders.matchQuery("categoryName",searchMap.get("category")));
        }

        String price= searchMap.get("price");
        if(!StringUtils.isEmpty(price))
        {

            String[] split=price.split("-");
            if (split[1].equalsIgnoreCase("*"))
            {
                boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(split[0]));
            } else{
                boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").from(split[0],true).to(split[1],true));
            }
        }

        //c.match查询
        //nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery("name",keywords));
        //设置主关键字查询
        nativeSearchQueryBuilder.withQuery(QueryBuilders.multiMatchQuery(keywords,"name","brandName","categoryName"));
        //构建过滤查询
        nativeSearchQueryBuilder.withFilter(boolQueryBuilder);

        //b.聚合查询
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuCategorygroup").field("categoryName").size(50));
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuBrandgroup").field("brandName").size(50));
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuSpecgroup").field("spec.keyword").size(50));



        //d.分页
          Integer pageNum=1;
          Integer pageSize=3;
          if (!StringUtils.isEmpty(searchMap.get("pageNum")))
          {
              try {
                  pageNum=Integer.valueOf(searchMap.get("pageNum"));
              } catch (NumberFormatException e) {
                  e.printStackTrace();
                  pageNum=1;
              }
          }
          //无论前端有没有传过来当前页码，都要查询，没有传过来页码默认当前查询第一页的数据
        nativeSearchQueryBuilder.withPageable(PageRequest.of(pageNum-1,pageSize));

          //e.排序
         String sortRule=searchMap.get("sortRule");
         String sortField=searchMap.get("sortField");
         if (!StringUtils.isEmpty(sortField)&&!StringUtils.isEmpty(sortField))
         {
             nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(sortField).order(sortRule.equals("DESC")?SortOrder.DESC:SortOrder.ASC));
         }
         //e 高亮显示
        nativeSearchQueryBuilder.withHighlightFields(new HighlightBuilder.Field("name"));
         nativeSearchQueryBuilder.withHighlightBuilder(new HighlightBuilder().preTags("<em style=\"color:red\">").postTags("</em>").fragmentSize(100));



        //3.构建查询对象
        NativeSearchQuery  query= nativeSearchQueryBuilder.build();

        //4. 执行查询
        AggregatedPage<SkuInfo> skuPage=elasticsearchTemplate.queryForPage(query, SkuInfo.class, new SearchResultMapperImpl());

        // 这里生成的DSL语句到后台是怎么样的

//        System.out.println(query.getQuery());
//        System.out.println(query.getFilter());
//        System.out.println(query.toString());
//
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        searchSourceBuilder.query(query.getQuery());
//
//        String queryPrint = searchSourceBuilder.toString();
//        System.out.println(queryPrint);

//////////////$$$$$$$$$$$$$$$$$$$$$$$$$$$
        //5.封装查询结果
        StringTerms stringTermsCategory= (StringTerms) skuPage.getAggregation("skuCategorygroup");
        StringTerms stringTermsBrand= (StringTerms) skuPage.getAggregation("skuBrandgroup");
        StringTerms stringTermsSpec= (StringTerms) skuPage.getAggregation("skuSpecgroup");

        List<String> categoryList=getStringsList(stringTermsCategory);
        List<String> brandList=getStringsList(stringTermsBrand);
        Map<String,Set> specList=getStringSetMap(stringTermsSpec);

        Map resultMap=new HashMap();

        resultMap.put("categoryList",categoryList);
        resultMap.put("brandList",brandList);
        resultMap.put("specList",specList);
        resultMap.put("rows",skuPage.getContent());
        resultMap.put("total",skuPage.getTotalElements());
        resultMap.put("totalPages",skuPage.getTotalPages());
            return resultMap;
    }

    private Map<String, Set> getStringSetMap(StringTerms stringTerms)
    {
        Map<String, Set> map=new HashMap<>();
        Map<String,String> specMap=new HashMap<>();
        Set<String> set;
        String key;
        String specJson;
        List<String> str;
        if (stringTerms!=null)
        {
            for (StringTerms.Bucket bucket:stringTerms.getBuckets())
            {
                key=bucket.getKeyAsString();
                specMap=JSON.parseObject(key,Map.class);
                for (Map.Entry<String,String> entry:specMap.entrySet())
                {
                    specJson=entry.getKey();
                    set=map.get(specJson);
                    if (set==null)
                    {
                        set=new HashSet<>();
                    }
                    set.add(entry.getValue());
                    map.put(specJson,set);
                }

            }
        }
        return map;
    }

    private List<String> getStringsList(StringTerms stringTerms)
    {
        List<String> list=new ArrayList<>();
        if (stringTerms!=null)
        {
            for (StringTerms.Bucket bucket:stringTerms.getBuckets())
            {
                list.add(bucket.getKeyAsString());
            }
        }
        return list;
    }
}
