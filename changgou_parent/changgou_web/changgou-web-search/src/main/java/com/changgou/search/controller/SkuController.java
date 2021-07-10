package com.changgou.search.controller;

import com.changgou.search.feign.SkuFeign;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
@Controller
@RequestMapping("/search")
public class SkuController {

    @Autowired
    private SkuFeign skuFeign;

    /**
     * 搜索
     * @param searchMap
     * @param model
     * @return
     */
    @GetMapping("/list")
    public String search(@RequestParam(required = false) Map searchMap, Model model)
    {
        Map resultMap=skuFeign.search(searchMap);
        model.addAttribute("result",resultMap);
        model.addAttribute("searchMap",searchMap);
        model.addAttribute("url",url(searchMap));
        return "search";
    }

    public String url(Map<String,String> searchMap){
        String url="/search/list";
        if (searchMap!=null&&searchMap.size()>0)
        {
            url+="？";
            for (Map.Entry<String,String> entry:searchMap.entrySet())
            {
                String key=entry.getKey();
                url+=key+"="+entry.getValue()+"&";
            }
        }
        return url.substring(0,url.length()-1);
    }
}
