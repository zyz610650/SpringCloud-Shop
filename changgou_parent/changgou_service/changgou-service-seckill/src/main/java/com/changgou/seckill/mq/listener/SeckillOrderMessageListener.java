package com.changgou.seckill.mq.listener;

import com.alibaba.fastjson.JSON;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.pojo.SeckillOrder;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import sun.dc.pr.PRError;

import java.util.Map;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
// 微信支付服务器回调信息处理
@Component
@RabbitListener(queues = {"${mq.seckillPay.order.queue}"})
public class SeckillOrderMessageListener {

    @Autowired
    private RedisTemplate redisTemplate;


    @RabbitHandler
    public void consumerMessage(String msg)
    {
        Map<String,String> result= JSON.parseObject(msg,Map.class);
        //return_code=SUCCESS
        String out_trade_no = result.get("out_trade_no");
        //业务结果
        String result_code = result.get("trade_state");
        Map<String,String> attach=JSON.parseObject(result.get("attach"),Map.class);
        String username=attach.get("username");

        if (result_code.equalsIgnoreCase("success"))
        {
            if (out_trade_no!=null)
            {
                SeckillGoods seckillStatus= (SeckillGoods) redisTemplate.boundHashOps("UserQueueStatus").get(username);
                SeckillOrder seckillOrder= (SeckillOrder) redisTemplate.boundHashOps("SeckullOrder").get(username);
                seckillOrder.setStatus("1");


            }
        }
    }

}
