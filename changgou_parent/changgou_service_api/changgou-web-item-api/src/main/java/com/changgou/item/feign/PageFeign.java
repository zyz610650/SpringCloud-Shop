package com.changgou.item.feign;

import com.changgou.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
@FeignClient("item")
@RequestMapping("/page")
public interface PageFeign {

    @RequestMapping("/createHtml/{id}")
    public Result createHtml(@PathVariable(name = "id") Long id);
}
