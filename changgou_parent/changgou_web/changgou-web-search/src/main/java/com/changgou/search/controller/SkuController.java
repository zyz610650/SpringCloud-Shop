package com.changgou.search.controller;

import com.changgou.entity.Page;
import com.changgou.search.feign.SkuFeign;
import com.changgou.search.pojo.SkuInfo;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
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
        Map resultMap=null;
       try{
            resultMap=skuFeign.search(searchMap);
           System.out.println(resultMap);
       }catch (Exception e)
       {
           System.out.println("11111111111");
           e.printStackTrace();
       }
        model.addAttribute("result",resultMap);
        model.addAttribute("searchMap",searchMap);
        model.addAttribute("url",url(searchMap));
        Page<SkuInfo> page=new Page<>(
                Long.parseLong(resultMap.get("totalPages").toString()),
                Integer.parseInt(resultMap.get("pageNum").toString()),
                Integer.parseInt(resultMap.get("pageSize").toString())
        );
        model.addAttribute("page",page);
        return "search";
    }

    public String url(Map<String,String> searchMap){
        String url="/search/list";
        url+="?";
        if (searchMap!=null&&searchMap.size()>0)
        {
            for (Map.Entry<String,String> entry:searchMap.entrySet())
            {
                if(entry.getKey().equalsIgnoreCase("sortField")||entry.getKey().equalsIgnoreCase("sortRule")||
                entry.getKey().equalsIgnoreCase("pageNum"))
                {
                    continue;
                }

                String key=entry.getKey();
                url+=key+"="+entry.getValue()+"&";
            }
        }
        System.out.println(url.substring(0,url.length()));
        return url.substring(0,url.length()-1);
    }
}
