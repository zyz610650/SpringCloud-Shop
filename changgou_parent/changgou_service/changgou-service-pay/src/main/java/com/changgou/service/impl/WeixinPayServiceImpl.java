package com.changgou.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.changgou.entity.Amount;
import com.changgou.entity.Pay;
import com.changgou.service.WeixinPayService;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.PrivateKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
@Service
public class WeixinPayServiceImpl implements WeixinPayService {


    @Autowired
    private HttpClient httpClient;



    @Value("${weixin.vxUrl}")
    private String vxUrl;

    @Value("${weixin.appid}")
    private String appid;

    @Value("${weixin.mchid}")
    private String mchid;
    @Value("${weixin.notifyurl}")
    private String notifyurl;
    /**
     * 返回创建支付二维码的url
     * @return
     * @throws Exception
     */
    @Override
    public Map<String,String> CreateNative(Integer total, String out_trade_no, String desc)  {
        //"https://api.mch.weixin.qq.com/v3/pay/transactions/native"
        HttpPost httpPost = new HttpPost(vxUrl);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("YYYY-MM-DDTHH:mm:ss+TIMEZONE");
        Pay pay=new Pay();
        pay.setAppid(appid);
        pay.setMchid(mchid);
        pay.setNotify_url(notifyurl);
        pay.setOut_trade_no(out_trade_no);
        pay.setDescription(desc);
        pay.setAmount(new Amount(total,"CNY"));

        // 请求body参数
        String reqdata = JSON.toJSONString(pay);
        System.out.println("发送的信息: "+reqdata);
        StringEntity entity;
        CloseableHttpResponse response = null;
        Map<String,String> dataMap = new HashMap<String,String>();
        try {
         entity = new StringEntity(reqdata);
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");

        //完成签名并执行请求
        response = (CloseableHttpResponse) httpClient.execute(httpPost);


            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) { //处理成功
                System.out.println("success,return body = " + EntityUtils.toString(response.getEntity()));
            } else if (statusCode == 204) { //处理成功，无返回Body
                System.out.println("success");
            } else {
                System.out.println("failed,resp code = " + statusCode+ ",return body = " + EntityUtils.toString(response.getEntity()));
                throw new IOException("request failed");
            }
            response.close();

            dataMap.put("code_url",EntityUtils.toString(response.getEntity()));
            dataMap.put("out_trade_no",out_trade_no);
            dataMap.put("total_fee",total.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return  dataMap;
    }

    @Override
    public Map<String, String> queryStatus(String out_trade_no) {
        StringBuilder url=new StringBuilder("https://api.mch.weixin.qq.com/v3/pay/transactions/id/");
        url.append(out_trade_no);

        List<NameValuePair> params=new ArrayList<>();
        URI uri=null;
        HttpGet httpGet=null;
        CloseableHttpResponse response=null;
        Map<String,String> resultMap=null;
        try {
            params.add(new BasicNameValuePair("mchid",mchid));
            uri=new URIBuilder(url.toString()).setParameters(params).build();
            httpGet=new HttpGet(uri);
            response= (CloseableHttpResponse) httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode()==200)
            {
                resultMap= (Map<String, String>) JSONObject.parse(response.getEntity().toString());
            }else {
                throw new RuntimeException("查询失败");
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultMap;
    }
}
