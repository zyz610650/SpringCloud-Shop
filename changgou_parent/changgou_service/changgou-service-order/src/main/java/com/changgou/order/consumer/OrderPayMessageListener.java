package com.changgou.order.consumer;

import com.alibaba.fastjson.JSON;
import com.changgou.order.pojo.Order;
import com.changgou.order.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
@Component
@RabbitListener(queues = {"${mq.pay.queue.order}"})
public class OrderPayMessageListener {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private OrderService orderService;

    @RabbitHandler
    public void consumerMessage(String msg)
    {
        Map<String,String> result= JSON.parseObject(msg,Map.class);
        //return_code=SUCCESS
        String out_trade_no = result.get("out_trade_no");
        //业务结果
        String result_code = result.get("trade_state");
        //支付成功
        if (result_code.equalsIgnoreCase("success"))
        {

            if (out_trade_no!=null)
            {
                Order order=new Order();
                order.setId(out_trade_no);
                order.setPayStatus("1");
                order.setTransactionId(result.get("transaction_id"));
                orderService.update(order);
            }else {
                
            }
        }

    }
}
