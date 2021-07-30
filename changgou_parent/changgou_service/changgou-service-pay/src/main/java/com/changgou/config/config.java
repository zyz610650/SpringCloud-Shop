package com.changgou.config;

import com.changgou.entity.IdWorker;
import com.changgou.entity.Page;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import org.apache.http.client.HttpClient;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
@Configuration
public class config {

    @Autowired
    private Environment environment;

    @Value("${weixin.privateKey}")
     String privateKey="";
    @Value("${weixin.mchId}")
     String mchId="";
    @Value("${weixin.mchSerialNo}")
     String mchSerialNo="";
    @Value("${weixin.apiV3Key}")
     String apiV3Key="";


    @Bean
    public IdWorker idWorker()
    {
        return new IdWorker();
    }

    /**
     * 创建httpClient
     * @return
     */
    @Bean
    public HttpClient httpClient()
    {
        HttpClient httpClient=null;
        // 加载商户私钥（privateKey：私钥字符串）
        try {
            PrivateKey merchantPrivateKey = null;
            merchantPrivateKey = PemUtil
                    .loadPrivateKey(new ByteArrayInputStream(privateKey.getBytes("utf-8")));


            // 加载平台证书（mchId：商户号,mchSerialNo：商户证书序列号,apiV3Key：V3密钥）
            AutoUpdateCertificatesVerifier verifier = null;

            verifier = new AutoUpdateCertificatesVerifier(
                    new WechatPay2Credentials(mchId, new PrivateKeySigner(mchSerialNo, merchantPrivateKey)),apiV3Key.getBytes("utf-8"));


            // 初始化httpClient
            httpClient = WechatPayHttpClientBuilder.create()
                    .withMerchant(mchId, mchSerialNo, merchantPrivateKey)
                    .withValidator(new WechatPay2Validator(verifier)).build();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return httpClient;
    }

    /**
     * 路由模式
     * @return
     */
    @Bean
    public DirectExchange basicExchange()
    {
        return new DirectExchange(environment.getProperty("mq.pay.exchangge.order"),true,false);
    }
    @Bean
    public Queue queueOrder()
    {
        return new Queue(environment.getProperty("mq.pay.queue.order"),true);
    }
    @Bean
    public Binding basicBinding()
    {
        return BindingBuilder.bind(queueOrder()).to(basicExchange()).with(environment.getProperty("mq.pay.routing.key"));
    }
}
