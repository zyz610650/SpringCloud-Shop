package com.changgou.order.mq.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.changgou.entity.Result;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.order.mq.queue.QueueConfig;
import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.OrderItemService;
import com.changgou.order.service.OrderService;
import com.changgou.order.service.impl.OrderServiceImpl;
import com.changgou.pay.feign.PayFeign;
import com.changgou.pay.pojo.Pay;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
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
@RabbitListener(queues = QueueConfig.QUQUE_MESSAGE_DELAY)
public class DelayMessageListener {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private PayFeign payFeign;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderServiceImpl orderServiceImpl;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private SkuFeign skuFeign;

    @RabbitHandler
    public void msg(@Payload Message message)
    {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("当前时间："+simpleDateFormat.format(new Date()));
        String msg=message.getBody().toString();
        System.out.println("收到信息: "+msg);
        Result result = payFeign.queryStatus(msg);
        Map<String,String> resMap= (Map<String, String>) result.getData();
        Order order=new Order();
        order.setId(msg);
        order.setTransactionId(resMap.get("transaction_id"));
        if (result.isFlag())
        {

            order.setPayStatus("1");
            order.setPayTime(new Date());
        }else
        {
            //支付失败
            //关闭微信服务器该订单信息
            if(!payFeign.deleteOrder(msg)) return;

            //回滚存
            // 根据订单Id查询 OrderItem
            OrderItem orderItem=new OrderItem();
            orderItem.setOrderId(msg);
            List<OrderItem> list= orderItemService.findList(orderItem);
            for (OrderItem item:list)
            {
                skuFeign.updateStock(orderItem.getSkuId().intValue(),-orderItem.getNum());
            }
            order.setId(msg);
            order.setIsDelete("1");
            order.setPayStatus("2");
            orderService.update(order);

        }


    }



}
