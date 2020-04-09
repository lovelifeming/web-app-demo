package com.zsm.websocket.kafka;

import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


/**
 * @Author: zengsm.
 * @Description: TODO()
 * @Date:Created in 2020-03-19 17:24.
 * @Modified By:
 */
@Component
public class KafkaConsumerListener
{
    public KafkaConsumerListener()
    {
        TimerManager();
    }

    private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;

    public void TimerManager()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date date = calendar.getTime();
        if (date.before(new Date()))
        {
            date = this.addDay(date, 1);
        }
        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                System.out.println("定时器触发：" + new Date().toString());
            }
        }, date, PERIOD_DAY);
    }

    public Date addDay(Date date, int num)
    {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @KafkaListener(topics = {"${kafka.consumer.topic}"})
    public void listen(ConsumerRecord<?, ?> record, Acknowledgment acknowledgment)
    {
        long offset = record.offset();
        logger.info("偏移量" + offset);
        String value = (String)record.value();
        String key = (String)record.key();
        logger.info("message value: " + value);
        logger.info("message key: " + key);
        if (value != null)
        {
            // TODO do something
            JSONObject object = JSONObject.parseObject(value);
            System.out.println(object);
            logger.info(object.toJSONString());
        }
        acknowledgment.acknowledge();
        logger.info("手动提交offset");
    }
}
