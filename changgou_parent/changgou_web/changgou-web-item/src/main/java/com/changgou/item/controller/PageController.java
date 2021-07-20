package com.changgou.item.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.item.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */

@RestController
@RequestMapping("/page")
public class PageController {

    @Autowired
    private PageService pageService;

    @RequestMapping("/item")
    public String getItemPage(String spuId)
    {
        return spuId;
    }
    /**
     * 根据SpuId 生成静态页
     * @param id
     * @return
     */
    @RequestMapping("/createHtml/{id}")
    public Result createHtml(@PathVariable(name = "id") Long id)
    {
        pageService.createHtml(id);

        return  new Result(true, StatusCode.OK,"OK");
    }
}
