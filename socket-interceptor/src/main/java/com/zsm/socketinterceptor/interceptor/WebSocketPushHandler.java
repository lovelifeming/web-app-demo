package com.zsm.socketinterceptor.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2020-04-20 16:53.
 * @Modified By:
 */
public class WebSocketPushHandler extends TextWebSocketHandler
{
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final List<WebSocketSession> userList = new ArrayList<>();

    /**
     * 用户进入系统监听
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session)
        throws Exception
    {
        logger.info("xxx用户进入系统。。。");
        logger.info("用户信息:" + session.getAttributes());
        Map<String, Object> map = session.getAttributes();
        for (String key : map.keySet())
        {
            logger.info("key:" + key + " and value:" + map.get(key));
        }
        userList.add(session);
    }

    /**
     * 处理用户请求
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)
        throws Exception
    {
        logger.info("系统处理xxx用户的请求信息。。。");
    }

    /**
     * 用户退出后的处理
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status)
        throws Exception
    {
        if (session.isOpen())
        {
            session.close();
        }
        userList.remove(session);
        logger.info("xxx用户退出系统。。。");
    }

    /**
     * 自定义函数
     * 给所有的在线用户发送消息
     */
    public void sendMessagesToUsers(TextMessage message)
    {
        for (WebSocketSession user : userList)
        {
            try
            {
                // isOpen()在线就发送
                if (user.isOpen())
                {
                    user.sendMessage(message);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
                logger.error(e.getLocalizedMessage());
            }
        }
    }
}
