package com.changgou.seckill.feign;
import com.changgou.entity.Result;
import com.changgou.seckill.pojo.SeckillGoods;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/****
 * @Author:admin
 * @Description:
 * @Date 2019/6/18 13:58
 *****/
@FeignClient(name="seckill")
@RequestMapping("/seckillGoods")
public interface SeckillGoodsFeign {


    /***
     * 多条件搜索品牌数据
     * @param seckillGoods
     * @return
     */
    @PostMapping(value = "/search" )
    Result<List<SeckillGoods>> findList(@RequestBody(required = false) SeckillGoods seckillGoods);

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}" )
    Result delete(@PathVariable Long id);

    /***
     * 修改SeckillGoods数据
     * @param seckillGoods
     * @param id
     * @return
     */
    @PutMapping(value="/{id}")
    Result update(@RequestBody SeckillGoods seckillGoods,@PathVariable Long id);


    /***
     * 根据ID查询SeckillGoods数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    Result<SeckillGoods> findById(@PathVariable Long id);

    /***
     * 查询SeckillGoods全部数据
     * @return
     */
    @GetMapping
    Result<List<SeckillGoods>> findAll();
}