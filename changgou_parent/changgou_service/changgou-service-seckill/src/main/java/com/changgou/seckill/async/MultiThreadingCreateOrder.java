package com.changgou.seckill.async;

import com.changgou.entity.IdWorker;
import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.pojo.SeckillOrder;
import com.changgou.seckill.pojo.SeckillStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */

@Component
public class MultiThreadingCreateOrder {

    @Autowired
    private IdWorker idWorker;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Async
    public void CreateOrder()
    {
        SeckillStatus seckillStatus = (SeckillStatus) redisTemplate.boundListOps("SeckillOrderQueue").rightPop();
        String time=seckillStatus.getTime();
        String username=seckillStatus.getUsername();
        Long secKillId=seckillStatus.getGoodsId();
        try {
            SeckillGoods seckillGoods= (SeckillGoods) redisTemplate.boundHashOps("SeckillGoods_"+time).get(secKillId);
            //检查库存
            if (seckillGoods==null||seckillGoods.getStockCount()<=0)
            {
                throw new RuntimeException("已售罄");
            }
            //更新库存
            seckillGoods.setStockCount(seckillGoods.getStockCount()-1);
            if (seckillGoods.getStockCount()<=0)
            {
                //更新缓存
                redisTemplate.boundHashOps("SeckillGoods_"+time).delete(secKillId);
                //更新到MySql
                seckillGoodsMapper.updateByPrimaryKey(seckillGoods);
            }else{
                //更新缓存
                redisTemplate.boundHashOps("SeckillGoods_"+time).put(secKillId,seckillGoods);
            }
            //创建订单
            SeckillOrder seckillOrder=new SeckillOrder();
            seckillOrder.setId(idWorker.nextId());
            seckillOrder.setCreateTime(new Date());
            seckillOrder.setUserId(username);
            seckillOrder.setMoney(seckillGoods.getCostPrice());
            seckillOrder.setStatus("0");
            //订单加到秒杀队列中
            redisTemplate.boundHashOps("SeckullOrder").put(username,seckillOrder);

            //抢单成功，更新抢单状态,排队->等待支付
            seckillStatus.setStatus(2);
            seckillStatus.setOrderId(seckillOrder.getId());
            seckillStatus.setMoney(Float.parseFloat(seckillOrder.getMoney()));
            redisTemplate.boundHashOps("UserQueueStatus").put(username,seckillStatus);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

    }
}
