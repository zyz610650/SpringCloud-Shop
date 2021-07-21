package com.changgou.oauth;


import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.Map;

public class CreateJwtTest {


    @Test
    public void testCreateToken()
    {
        String key_location="changgou.jks";
        String key_password="changgou";
        String keypwd="changgou";
        String alias="changgou";

        ClassPathResource resource=new ClassPathResource(key_location);
        KeyStoreKeyFactory keyStoreKeyFactory=new KeyStoreKeyFactory(resource,key_password.toCharArray());

        KeyPair keyPair=keyStoreKeyFactory.getKeyPair(alias,keypwd.toCharArray());

        //
        RSAPrivateKey rsaPrivateKey= (RSAPrivateKey) keyPair.getPrivate();

        Map<String,Object>
    }
}
