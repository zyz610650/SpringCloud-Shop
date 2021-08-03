package com.changgou.order.mq.queue;

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
 //这里队列名字和路由名字一致
    public static final String QUEUE_MESSAGE="queue.message";
    public static final String DLX_EXCHANGE="dlx.exchange";
    public static final String QUQUE_MESSAGE_DELAY="queue.message.delay";

    @Bean
    public Queue messageQueue()
    {
        return new Queue(QueueConfig.QUEUE_MESSAGE,true);
    }

    @Bean
    public Queue delayQueue()
    {
        return QueueBuilder.durable(QueueConfig.QUQUE_MESSAGE_DELAY)
                .withArgument("x-dead-letter-exchange",DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key",QUQUE_MESSAGE_DELAY)
                .build();
    }

    @Bean
    public DirectExchange directExchange()
    {

        return new DirectExchange(DLX_EXCHANGE,true,false);
    }


    @Bean
    public Binding basicBinding()
    {
        return BindingBuilder.bind(messageQueue()).to(directExchange()).with("QUEUE_MESSAGE");
    }


}
