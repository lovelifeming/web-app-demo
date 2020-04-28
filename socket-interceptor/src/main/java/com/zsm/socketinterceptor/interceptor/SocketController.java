package com.zsm.socketinterceptor.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.UUID;


/**
 * @Author: zengsm.
 * @Description: https://blog.csdn.net/seafishloving/article/details/74523246
 * @Date:Created in 2020-04-20 17:29.
 * @Modified By:
 */
@Controller
public class SocketController
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public SimpMessagingTemplate template;

    @RequestMapping("/topic")
    public String topic()
    {
        return "topic";
    }

    @MessageMapping("/subscribe")
    public void subscribe(String message)
    {
        for (int i = 1; i <= 5; i++)
        {
            //广播使用convertAndSend方法，第一个参数为目的地，和js中订阅的目的地要一致
            template.convertAndSend("/topic/getResponse", "Hi ,I'm server" + i);
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    @MessageMapping("/welcome") //当浏览器向服务端发送请求时,通过@MessageMapping映射/welcome这个地址,类似于@ResponseMapping
    @SendTo("/topic/getResponse")//当服务器有消息时,会对订阅了@SendTo中的路径的浏览器发送消息
    public ResponseMessage say(RequestMessage message)
    {
        logger.info("Web socket message is {}", message);
        try
        {
            //睡眠1秒
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return new ResponseMessage(UUID.randomUUID().toString(), "", new Date(), message.getMessage());
    }
}
