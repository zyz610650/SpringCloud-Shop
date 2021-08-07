package com.changgou.pay.feign;

import com.changgou.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
@FeignClient("pay")
@RequestMapping("/weixin/pay")
public interface PayFeign {
    @RequestMapping("/del")
    public boolean deleteOrder(String out_trade_no);

    @RequestMapping("/create/native")
    public Result<Map<String,String>> createNative(Integer money, String desc,String exchange,String routingKey);

    @RequestMapping("/query")
    public Result queryStatus(String out_trade_no);
}
