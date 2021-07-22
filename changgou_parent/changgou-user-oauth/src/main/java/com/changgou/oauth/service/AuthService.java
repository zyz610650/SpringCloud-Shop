package com.changgou.oauth.service;

import com.changgou.oauth.util.AuthToken;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public interface AuthService {

    AuthToken login(String username,String pwd,String clientId,String clientSecret);
}
