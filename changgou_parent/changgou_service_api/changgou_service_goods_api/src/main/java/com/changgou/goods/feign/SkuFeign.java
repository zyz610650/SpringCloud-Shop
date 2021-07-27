package com.changgou.goods.feign;

import com.changgou.entity.CacheKey;
import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.goods.pojo.Category;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 *
 */
@FeignClient(name = "goods")
@RequestMapping("/sku")
public interface SkuFeign {
    /***
     * 根据审核状态查询Sku
     * @param status
     * @return
     */
    @GetMapping("/status/{status}")
    Result<List<Sku>>  findByStatus(@PathVariable String status);


    @GetMapping("/{id}")
    public Result<Sku> findById(@PathVariable Long id);

    /**
     *
     * 根据条件查询sku信息
     * @param sku
     * @return
     */
    @PostMapping("/search")
    Result<List<Sku>> findList(@RequestBody(required = false) Sku sku);



}
