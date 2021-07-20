package com.changgou.entity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public class JwtUtil {
    public static final Long JWT_TTL=3600000L;
    public static final String JWT_KEY="itcast";

    public static String createJWT(String id,String subject,Long ttlMllis)
    {
        SignatureAlgorithm signatureAlgorithm=SignatureAlgorithm.HS256;
        long nowMillis=System.currentTimeMillis();
        Date now=new Date(nowMillis);
        if (ttlMllis==null)
        {
            ttlMllis=JwtUtil.JWT_TTL;
        }
        long expMillis=nowMillis+ttlMllis;
        Date expDate=new Date(expMillis);

        SecretKey secretKey=generateKey();
        System.out.println("secretKey： "+secretKey);
        JwtBuilder builder=Jwts.builder().setId(id)
                .setSubject(subject)
                .setIssuer("admin")
                .setIssuedAt(now)
                .signWith(signatureAlgorithm,secretKey)
                .setExpiration(expDate);
        System.out.println("builder.compact(): "+builder.compact());
        return builder.compact();
    }

    private static SecretKey generateKey() {

        byte[] encodedKey= Base64.getEncoder().encode(JWT_KEY.getBytes());
        SecretKey key=new SecretKeySpec(encodedKey,0,encodedKey.length,"AES");
        return key;
    }

    public static Claims parseJWT(String jwt)
    {
        SecretKey secretKey=generateKey();
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt).getBody();
    }
}
