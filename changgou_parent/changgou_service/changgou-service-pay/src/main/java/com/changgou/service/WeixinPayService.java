package com.changgou.service;

import java.io.IOException;
import java.util.Map;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public interface WeixinPayService {
    public Map<String,String> CreateNative(Integer total, String out_trade_no, String desc);
    public Map<String,String> queryStatus(String out_trade_no);
    public boolean deleteOrder(String out_trade_no);
}
