package com.changgou.seckill.feign;
import com.changgou.entity.Result;
import com.changgou.seckill.pojo.SeckillOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/****
 * @Author:admin
 * @Description:
 * @Date 2019/6/18 13:58
 *****/
@FeignClient(name="seckill")
@RequestMapping("/seckillOrder")
public interface SeckillOrderFeign {



    /***
     * 多条件搜索品牌数据
     * @param seckillOrder
     * @return
     */
    @PostMapping(value = "/search" )
    Result<List<SeckillOrder>> findList(@RequestBody(required = false) SeckillOrder seckillOrder);

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}" )
    Result delete(@PathVariable Long id);

    /***
     * 修改SeckillOrder数据
     * @param seckillOrder
     * @param id
     * @return
     */
    @PutMapping(value="/{id}")
    Result update(@RequestBody SeckillOrder seckillOrder, @PathVariable Long id);

    /***
     * 新增SeckillOrder数据
     * @param seckillOrder
     * @return
     */
    @PostMapping
    Result add(@RequestBody SeckillOrder seckillOrder);

    /***
     * 根据ID查询SeckillOrder数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    Result<SeckillOrder> findById(@PathVariable Long id);

    /***
     * 查询SeckillOrder全部数据
     * @return
     */
    @GetMapping
    Result<List<SeckillOrder>> findAll();
}