package com.changgou.oauth.service.impl;

import com.changgou.oauth.service.AuthService;
import com.changgou.oauth.util.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


import java.io.IOException;
import java.util.Map;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private RestTemplate restTemplate ;

    @Override
    public AuthToken login(String username, String pwd, String clientId, String clientSecret) {
        System.out.println(2);
        ServiceInstance serviceInstance=loadBalancerClient.choose("user-auth");
        if (serviceInstance==null) throw new RuntimeException("找不到对应的服务");
        String path=serviceInstance.getUri().toString()+"/oauth/token";
        System.out.println("URI: "+serviceInstance.getUri());
        System.out.println("Host: "+serviceInstance.getHost());
        MultiValueMap<String,String> formData=new LinkedMultiValueMap<>();
        formData.add("grant_type","password");
        formData.add("username",username);
        formData.add("password",pwd);

        MultiValueMap<String,String> header=new LinkedMultiValueMap<>();
        header.add("Authorization",httpbasic(clientId,clientSecret));
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode()!=400&&response.getRawStatusCode()!=401)
                {
                    super.handleError(response);
                }
            }
        });
        Map map=null;

        try {
            ResponseEntity<Map> mapResponseEntity=restTemplate.exchange(path, HttpMethod.POST,new HttpEntity<>(formData,header),Map.class);
            map=mapResponseEntity.getBody();
            System.out.println(map.toString());
        } catch (RestClientException e) {

            throw new RuntimeException(e);
        }
        if (map==null||map.get("access_token")==null||map.get("refresh_token")==null||map.get("jti")==null)
        {
            throw new RuntimeException("创建令牌失败!");
        }
        AuthToken authToken=new AuthToken();
        authToken.setAccessToken((String) map.get("access_token"));
        authToken.setJti((String) map.get("jti"));
        authToken.setRefreshToken((String) map.get("refresh_token"));
        return authToken;
    }

    private String httpbasic(String clientId,String clientSecret)
    {
        String str=clientId+":"+clientSecret;
        byte[] encode= Base64Utils.encode(str.getBytes());
        return "Basic "+new String(encode);
    }
}
