package com.changgou.order.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;

import com.changgou.entity.TokenDecode;
import com.changgou.order.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.TokenStore;
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
    @Autowired
    private TokenDecode tokenDecode;
    //这里用户信息都保存在SecurityContextHolder.getContext().getAuthentication()。getDetails 由SprigSecurity框架
    //保存,就不需要每次穿过啦用户名了

    /**
     *
     * @param num
     * @param id skuId
     * @param
     * @return
     */
    @PostMapping({"/add","/delete"})
    public Result add(Integer num,Long id)
    {
       String username=tokenDecode.getUserInfo().get("username");
        cartService.add(num,id,username);
        return new Result(true, StatusCode.OK,"加入购物车成功！");
    }

    @GetMapping("/list")
    public Result findAll()
    {
        String username=tokenDecode.getUserInfo().get("username");
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
