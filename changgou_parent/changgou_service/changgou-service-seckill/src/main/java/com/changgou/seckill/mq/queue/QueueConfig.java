package com.changgou.seckill.mq.queue;

import com.rabbitmq.client.AMQP;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.*;


/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
@Configuration
public class QueueConfig {
    public static final String SECKILL_QUQUQ="queue.seckill";
    public static final String SECKILL_DELAY_QUQUQ="queue.seckill.delay";
    public static final String SECKILL_EXCHANGE="exchange.seckill";
    public static final String DLX_EXCHANG="exchange.dlxSeckill";
    public static final String SECKILL_ROUTING="queue.seckill";
    public static final String SECKILL_DLX_ROUTING="queue.dlxSeckill";
    @Bean
    public Queue seckillQueue()
    {
        return new Queue(QueueConfig.SECKILL_QUQUQ,true);
    }
    @Bean
    public Queue delayQueue()
    {
       return QueueBuilder.durable(SECKILL_DELAY_QUQUQ)
               .withArgument("x-dead-letter-exchange",DLX_EXCHANG)
               .withArgument("x-dead-letter-routing-key",SECKILL_DLX_ROUTING)
               .build();
    }

    @Bean
    public DirectExchange directExchange()
    {
        return new DirectExchange(SECKILL_EXCHANGE,true,false);
    }

    @Bean
    public Binding basicBinding()
    {
        return BindingBuilder.bind(seckillQueue()).to(directExchange()).with(QueueConfig.SECKILL_ROUTING);
    }
}
