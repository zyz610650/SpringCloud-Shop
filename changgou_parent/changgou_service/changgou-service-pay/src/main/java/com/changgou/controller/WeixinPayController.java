package com.changgou.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.changgou.entity.IdWorker;
import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.service.WeixinPayService;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
@RestController
@CrossOrigin
@RequestMapping("/weixin/pay")
public class WeixinPayController {

    @Autowired
    private WeixinPayService weixinPayService;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${mq.pay.exchange.order}")
    private String exchange;
    @Value("${mq.pay.queue.order}")
    private String queue;
    @Value("${mq.pay.routing.key}")
    private String routing;

    /**
     * 创建支付链接
     * @param money
     * @param desc
     * @return
     */
    @RequestMapping("/create/native")
    public Result createNative(Integer money,String desc)
    {
     String id=String.valueOf(idWorker.nextId());
     Map<String,String>  resultMap=weixinPayService.CreateNative(money,id,desc);
     return new Result(true, StatusCode.OK,"支付链接创建成功!",resultMap);
    }

    /**
     * 查询订单支付状况
     */
    @RequestMapping("/query")
    public Result queryStatus(String out_trade_no)
    {
        Map<String,String> resultMap=weixinPayService.queryStatus(out_trade_no);
        return new Result(true,StatusCode.OK,"查询成功",resultMap);
    }

    @RequestMapping(value = "/notify/url")
    public String notifyUrl(HttpServletRequest request)
    {
        //应答微信服务器
        Map respMap = new HashMap();
        //签名认证
      //  String Authorization=request.getHeader("Authorization");

        InputStream inputStream;
        try {
            inputStream=request.getInputStream();
            ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
            byte[] buffer=new byte[1024];
            int len=0;
            while ((len=inputStream.read(buffer))!=-1)
            {
                outputStream.write(buffer,0,len);
            }
            outputStream.close();
            inputStream.close();
            String result=new String(outputStream.toByteArray(),"utf-8");
            Map<String,Object> map= (Map<String, Object>) JSONObject.parse(result);
            //获取加密数据
            Map<String,String> resource= (Map<String, String>) map.get("resource");
            String dataResult=resource.get("ciphertext");
            //将消息发送给RabbitMQ
            rabbitTemplate.convertAndSend(exchange,routing,JSON.toJSONString(dataResult));
            
            //解密判断是否有问题
            respMap.put("code","SUCCESS");
            respMap.put("message","成功");

            //修改订单状态为支付
        } catch (IOException e) {
            respMap.put("code","FAIL");
            respMap.put("message","失败");
            e.printStackTrace();
        }
        return (String) JSON.toJSON(respMap);
    }

    /**
     * 删除微信服务器订单
     * @param out_trade_no
     * @return
     */
    @RequestMapping("/del")
    public boolean deleteOrder(String out_trade_no)
    {
        return weixinPayService.deleteOrder(out_trade_no);
    }




}
