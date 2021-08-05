package com.changgou.seckill;

import com.changgou.entity.IdWorker;
import com.changgou.entity.TokenDecode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
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
@MapperScan(basePackages = {"com.changgou.seckill.dao"})
@EnableScheduling
public class SeckillApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeckillApplication.class);
    }

    @Bean
    public IdWorker idWorker()
    {
        return new IdWorker(1,1);
    }
    @Bean
    public TokenDecode tokenDecode()
    {
        return new TokenDecode();
    }
}
