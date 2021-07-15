package com.changgou.item.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.entity.Result;
import com.changgou.goods.feign.CategoryFeign;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.goods.pojo.Template;
import com.changgou.item.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
@Service
public class PageServiceImpl implements PageService {

    @Autowired
    private SpuFeign spuFeign;

    @Autowired
    private CategoryFeign categoryFeign;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${pagepath}")
    private String pagepath;


    /**
     * 构建数据模型
     * @param spuID
     * @return
     */
    private Map<String,Object> buildDataModel(Long spuID)
    {
        Result<Spu> result=spuFeign.findById(spuID);
        Map<String,Object> map=new HashMap<>();
        Spu spu=result.getData();

        map.put("category1",categoryFeign.findById(spu.getCategory1Id()).getData());
        map.put("category2",categoryFeign.findById(spu.getCategory2Id()).getData());
        map.put("category3",categoryFeign.findById(spu.getCategory3Id()).getData());

        if (spu.getImages()!=null)
        {
            map.put("imageList",spu.getImages().split(","));
        }
        map.put("specificationList", JSON.parseObject(spu.getSpecItems(),Map.class));
        map.put("spu",spu);

        //根据spuID查询sku集合
        Sku skuCondition=new Sku();
        skuCondition.setSpuId(spu.getId());
        map.put("skuList",skuFeign.findList(skuCondition).getData());
        return map;

    }


    /**
     * 生成静态页
     * @param id
     */
    @Override
    public void createHtml(Long id) {
        Context context=new Context();
        Map<String,Object> dataModel=buildDataModel(id);
        context.setVariables(dataModel);

        File dir= new File(pagepath);
        if (!dir.exists()) dir.mkdirs();
        File dest=new File(dir,id+".html");
        try {
            PrintWriter writer=new PrintWriter(dest,"UTF-8");
            templateEngine.process("item",context,writer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
