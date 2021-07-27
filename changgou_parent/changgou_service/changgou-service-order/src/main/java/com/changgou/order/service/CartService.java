package com.changgou.order.service;

import com.changgou.order.pojo.OrderItem;

import java.util.List;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public interface CartService {
    void add(Integer num,Long id,String username);

    List<OrderItem> findAll(String username);

    void delete(String username,Long skuId);

}
