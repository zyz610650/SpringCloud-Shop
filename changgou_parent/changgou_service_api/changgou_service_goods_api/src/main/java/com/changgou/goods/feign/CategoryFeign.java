package com.changgou.goods.feign;

import com.changgou.entity.Result;
import com.changgou.goods.pojo.Category;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
@FeignClient("goods")
@RequestMapping("/category")
public interface CategoryFeign {

    /**
     * 根据ID获取商品种类
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    Result<Category> findById(@PathVariable Integer id);
}
