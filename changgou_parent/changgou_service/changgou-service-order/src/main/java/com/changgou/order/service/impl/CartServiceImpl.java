package com.changgou.order.service.impl;

import com.changgou.entity.Result;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.Column;
import java.util.List;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SkuFeign skuFeign;
    @Autowired
    private SpuFeign spuFeign;

    @Override
    public void add(Integer num, Long id, String username) {

        if (num<=0)//删除
        {
            redisTemplate.boundHashOps("Cart_"+username).delete(id);
            return;
        }
        Result<Sku> resultSku=skuFeign.findById(id);
        if (!resultSku.isFlag()&&resultSku==null) return;

        Result<Spu> resultSpu=spuFeign.findById(resultSku.getData().getSpuId());
        OrderItem orderItem=skuToOrderItem(resultSpu.getData(),resultSku.getData(),num);
        redisTemplate.boundHashOps("Cart_"+username).put(id,orderItem);
    }

    @Override
    public List<OrderItem> findAll(String username) {

        List<OrderItem> list = redisTemplate.boundHashOps("Cart_" + username).values();
        return list;
    }

    @Override
    public void delete(String username, Long skuId) {
        redisTemplate.boundHashOps("Cart_"+username).delete(skuId);
    }

    private OrderItem skuToOrderItem(Spu spu,Sku sku,Integer num)
    {
        OrderItem orderItem=new OrderItem();
        orderItem.setSpuId(sku.getSpuId());
        orderItem.setSkuId(sku.getId());
        orderItem.setName(sku.getName());
        orderItem.setPrice(sku.getPrice());
        orderItem.setNum(num);
        orderItem.setMoney(num*orderItem.getPrice());
        orderItem.setPayMoney(num*orderItem.getPrice());
        orderItem.setImage(sku.getImage());
        orderItem.setWeight(sku.getWeight()*num);
        orderItem.setCategoryId1(spu.getCategory1Id());
        orderItem.setCategoryId2(spu.getCategory2Id());
        orderItem.setCategoryId3(spu.getCategory3Id());
        return orderItem;
    }
}
