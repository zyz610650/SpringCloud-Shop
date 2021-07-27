package com.changgou.order.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;

import com.changgou.order.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Stack;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
@RestController
@CrossOrigin
@RequestMapping("/cart")
public class CartController {


    @Autowired
    private CartService cartService;

    /**
     *
     * @param num
     * @param id skuId
     * @param username
     * @return
     */
    @PostMapping({"/add","/delete"})
    public Result add(Integer num,Long id,String username)
    {
        if (username==null) username="itheima";
        cartService.add(num,id,username);
        return new Result(true, StatusCode.OK,"加入购物车成功！");
    }

    @GetMapping("/list")
    public Result findAll(String username)
    {
        return new Result(true,StatusCode.OK,"购物车列表成功！",cartService.findAll(username));
    }

    /**
     * skuId
     * @param username
     * @param id
     * @return
     */
    @DeleteMapping
    public Result delete(String username,Long id,Integer num)
    {
        cartService.delete(username,id);
        return new Result(true,StatusCode.OK,"从购物车中删除成功");

    }
}
