package com.zsm.socketinterceptor.interceptor;

import org.apache.logging.log4j.Logger;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static org.apache.logging.log4j.LogManager.getLogger;


/**
 * @Author: zeng.
 * @Date:Created in 2020-04-27 16:46.
 * @Description:
 */
public class WebSocketNotificationHandler extends TextWebSocketHandler
{

    private static final Logger log = getLogger(WebSocketNotificationHandler.class);

    public static final Map<String, WebSocketSession> WS_HOLDER = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session)
        throws Exception
    {
        String httpSessionId = (String)session.getAttributes().get(
            HttpSessionHandshakeInterceptor.HTTP_SESSION_ID_ATTR_NAME);
        WS_HOLDER.put(httpSessionId, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)
        throws Exception
    {
        log.info("handleTextMessage={}", message.getPayload());
    }

    public static void usesWSPush(String sessionid, String content)
    {
        WebSocketSession wssession = WebSocketNotificationHandler.WS_HOLDER.get(sessionid);
        if (Objects.nonNull(wssession))
        {
            TextMessage textMessage = new TextMessage(content);
            try
            {
                wssession.sendMessage(textMessage);
            }
            catch (IOException | IllegalStateException e)
            {
                //WebSocketNotificationHandler.SESSIONS.remove(sessionid);
            }
        }
    }
}
