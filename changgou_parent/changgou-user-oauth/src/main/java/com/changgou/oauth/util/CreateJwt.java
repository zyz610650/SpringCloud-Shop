package com.changgou.oauth.util;

import com.alibaba.fastjson.JSON;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public class CreateJwt {
    public void CreateToken()
    {
        String key_location="changgou.jks";
        String key_password="changgou";
        String keypwd="changgou";
        String alias="changgou";

        ClassPathResource resource=new ClassPathResource(key_location);
        KeyStoreKeyFactory keyStoreKeyFactory=new KeyStoreKeyFactory(resource,key_password.toCharArray());

        KeyPair keyPair=keyStoreKeyFactory.getKeyPair(alias,keypwd.toCharArray());

        RSAPrivateKey rsaPrivateKey= (RSAPrivateKey) keyPair.getPrivate();
        Map<String,Object> tokenMap=new HashMap<>();
        tokenMap.put("id",1);
        tokenMap.put("name","itheima");
        tokenMap.put("roles","ROLE_VIP,ROLE_USER");

        Jwt jwt= JwtHelper.encode(JSON.toJSONString(tokenMap),new RsaSigner(rsaPrivateKey));
        String encoded=jwt.getEncoded();
        System.out.println(encoded);
    }
}
