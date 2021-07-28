package com.changgou.order;

import com.changgou.entity.FeignInterceptor;
import com.changgou.entity.IdWorker;
import com.changgou.entity.TokenDecode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = {"com.changgou.order.dao"})
@EnableFeignClients(basePackages = {"com.changgou.goods.feign","com.changgou.user.feign"})
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class);
    }

    @Bean
    public FeignInterceptor feignInterceptor()
    {
        return new FeignInterceptor();
    }

    @Bean
    public TokenDecode tokenDecode()
    {
        return new TokenDecode();
    }
    @Bean
    public IdWorker IdWorker()
    {
        return new IdWorker();
    }


}

