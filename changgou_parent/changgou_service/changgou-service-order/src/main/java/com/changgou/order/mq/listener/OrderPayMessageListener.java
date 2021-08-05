package com.changgou.order.mq.listener;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.OrderItemService;
import com.changgou.order.service.OrderService;
import com.changgou.pay.feign.PayFeign;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
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
    @Autowired
    private PayFeign payFeign;
    @Autowired
    private SkuFeign skuFeign;
    @Autowired
    private OrderItemService orderItemService;

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

                //关闭微信服务器该订单信息
                if(!payFeign.deleteOrder(out_trade_no)) return;

                //回滚存
                // 根据订单Id查询 OrderItem
                OrderItem orderItem=new OrderItem();
                orderItem.setOrderId(out_trade_no);
                List<OrderItem> list= orderItemService.findList(orderItem);
                for (OrderItem item:list)
                {
                    skuFeign.updateStock(orderItem.getSkuId().intValue(),-orderItem.getNum());
                }

                //支付失败 更新订单信息
                Order order=new Order();
                order.setId(out_trade_no);
                order.setIsDelete("1");
                order.setPayStatus("2");
                order.setTransactionId(result.get("transaction_id"));
                orderService.update(order);
            }
        }

    }
}
