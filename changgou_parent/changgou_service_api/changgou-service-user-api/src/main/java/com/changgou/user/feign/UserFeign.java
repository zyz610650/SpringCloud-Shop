package com.changgou.user.feign;

import com.changgou.entity.Result;
import com.changgou.user.pojo.User;
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

@FeignClient(name = "user")
@RequestMapping("/user")
public interface UserFeign {

    @GetMapping("/load/{id}")
    public Result<User> findById(@PathVariable String id) ;

    @GetMapping("/points/add/{points}")
    public Result addPoints(@PathVariable Integer points);
}
