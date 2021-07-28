package com.changgou.entity;

import com.alibaba.fastjson.JSON;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
@Component
public class TokenDecode {

    private static String PUBLIC_KEY="public.key";
    private static String publicKey="";

    public  String getPublicKey()
    {
        if (!publicKey.isEmpty()) return publicKey;
        Resource resource=new ClassPathResource(PUBLIC_KEY);
        try {
            InputStreamReader inputStreamReader=new InputStreamReader(resource.getInputStream());
            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
            publicKey=bufferedReader.lines().collect(Collectors.joining("\n"));
            return publicKey;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public Map<String,String> decodeToken(String token)
    {
        Jwt jwt= JwtHelper.decodeAndVerify(token,new RsaVerifier(getPublicKey()));
        String claims=jwt.getClaims();
        return JSON.parseObject(claims,Map.class);
    }

    public Map<String,String> getUserInfo()
    {
        OAuth2AuthenticationDetails details= (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        return decodeToken(details.getTokenValue());
    }

 }
