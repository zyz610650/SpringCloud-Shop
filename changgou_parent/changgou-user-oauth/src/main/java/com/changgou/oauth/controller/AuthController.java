package com.changgou.oauth.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.oauth.service.AuthService;
import com.changgou.oauth.util.AuthToken;
import com.changgou.oauth.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
@RestController
@RequestMapping("/user")
public class AuthController {
//    ttl: 3600  #token存储到redis的过期时间
//    clientId: changgou
//    clientSecret: changgou
//    cookieDomain: localhost
//    cookieMaxAge: -1
    @Value("${auth.clientId}")
    private String clientId;
    @Value("${auth.clientSecret}")
    private String clientSecret;
    @Value("${auth.cookieDomain}")
    private String cookieDomain;
    @Value("${auth.cookieMaxAge}")
    private int cookieMaxAge;

    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public Result login(String username,String password)
    {
        System.out.println(1);
        if (StringUtils.isEmpty(username))
        {
            throw new RuntimeException("用户名不能为空！");
        }
        if (StringUtils.isEmpty(password))
            throw new RuntimeException("密码不能为空!");

        AuthToken authToken=authService.login(username,password,clientId,clientSecret);

        // 这有问题 RequestContextHolder是什么
        HttpServletResponse httpServletResponse= ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
        CookieUtil.addCookie(httpServletResponse,cookieDomain,"/","Authorization",authToken.getAccessToken(),cookieMaxAge,false);
        return new Result(true, StatusCode.OK,"登录成功！");
    }

}
