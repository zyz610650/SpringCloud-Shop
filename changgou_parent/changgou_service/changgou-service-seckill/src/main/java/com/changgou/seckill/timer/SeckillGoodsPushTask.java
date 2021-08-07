package com.changgou.seckill.timer;

import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
@Component
public class SeckillGoodsPushTask {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;


    @Scheduled(cron = "0/10 * * * * ?")
    public  void loadGoodsPushRedis(){
        List<Date> dates=getDateMenus();
        Date endTime;
        String nameSpace;
        for (Date startTime:dates)
        {
            nameSpace=data2str(startTime);
            endTime=addHour(startTime,2);
            Example example=new Example(SeckillGoods.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("status",1);
            criteria.andGreaterThan("stockCount",0);
            criteria.andGreaterThanOrEqualTo("startTime",startTime);
            Set<String> keys=redisTemplate.boundHashOps("SeckillGoods_"+nameSpace).keys();
            if (keys.size()>0&&keys!=null)
            {
                criteria.andNotIn("id",keys);
            }
            List<SeckillGoods> seckillGoods=seckillGoodsMapper.selectByExample(example);
            for (SeckillGoods seckillGoods1:seckillGoods)
            {
                redisTemplate.boundHashOps("SeckillGoods_"+nameSpace).put(seckillGoods1.getId(),seckillGoods1);
                redisTemplate.expireAt("SeckillGoods_"+nameSpace,endTime);
                redisTemplate.boundHashOps("SeckillGoodsCount").increment(seckillGoods1.getId(),1);
                System.out.println(seckillGoods1.toString());
            }


        }

    }

    public static String data2str(Date date)
    {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("YYMMDDHH");
        return simpleDateFormat.format(date);
    }
    public static Date addHour(Date date,int hour)
    {
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR,2);
        return calendar.getTime();
    }
    public static List<Date> getDateMenus()
    {
        List<Date> list=new ArrayList<>();
        Calendar calendar=Calendar.getInstance();
        Date nowHour=getCurHour();
        calendar.setTime(nowHour);
        list.add(nowHour);
        for(int i=0;i<4;i++)
        {
            calendar.add(Calendar.HOUR,2);
            list.add(calendar.getTime());
        }
        return list;
    }
    // 获取当前小时属于哪个小时区间
    public static  Date getCurHour()
    {
        List<Date> datelist=getDate();

        Date nowHour=new Date();
        Date curHour;
        for(int i=11;i>=0;i--)
        {
            curHour=datelist.get(i);
            if (nowHour.getTime()>=curHour.getTime())
            {
                nowHour=curHour;
                break;
            }
        }
        return nowHour;
    }

    public static  List<Date> getDate()
    {
        List<Date> list=new ArrayList<>();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        Date date=new Date();
        calendar.set(Calendar.HOUR,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        list.add(calendar.getTime());
        for(int i=0;i<11;i++)
        {
            calendar.add(Calendar.HOUR,2);
            list.add(calendar.getTime());
        }

        return list;
    }

}
