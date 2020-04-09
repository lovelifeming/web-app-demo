package com.zsm.websocket.socket;//package com.digitized.server.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @Author :zengsm.
 * @Description :
 * @Date:Created in 2019/5/27 16:38.
 * @Modified By :
 */

@ServerEndpoint("/zsm/{userId}")
@Component
public class WebSocket
{
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocket.class);

    private static int ON_LINE_COUNT = 0;

    private static Map<String, WebSocket> SESSONID_WEBSOCKETSET_MAP = new ConcurrentHashMap<String, WebSocket>();

    private static Map<String, String> SESSION_USERID = new ConcurrentHashMap<String, String>();

    private Session session;

    /**
     * 用户建立连接时触发,连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session)
    {
        System.out.println(userId + " 连接连接成功 onOpen");
        this.session = session;
        Map<String, List<String>> requestParameterMap = session.getRequestParameterMap();
        List<String> list = requestParameterMap.get("username");
        String username = list.get(0);
        String sessonid = session.getId();
        SESSONID_WEBSOCKETSET_MAP.put(sessonid, this);
        SESSION_USERID.put(sessonid, userId);
        System.out.println(String.format("sessonid=%s userId=%s", sessonid, userId));
        addOnlineCount();
    }

    /**
     * 连接关闭调用的方法,监听用户断开连接事件
     */
    @OnClose
    public void onClose()
    {
        System.out.println("onClose 关闭连接");
        //将map中的sessionid移除
        SESSION_USERID.remove(this.session.getId());
        subOnlineCount();
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数，会话session
     */
    @OnMessage
    public void onMessage(String message, Session session)
    {
        System.out.println("onMessage 客服端返回数据：" + message);
        Map<String, List<String>> requestParameterMap = session.getRequestParameterMap();
        List<String> username = requestParameterMap.get("username");
        String name = username.get(0);      // 获取发送对象名称
        String sessionid = session.getId();

        String[] sendTo = message.trim().split("##");   // 获取发送给指定对象消息的Id
        String userIds = sendTo[sendTo.length - 1];
        String msg = message.substring(0, message.lastIndexOf("##"));
        HashSet<String> userIdSet = new HashSet<>();
        if (userIds.contains(","))
        {
            String[] id = userIds.split(",");
            userIdSet.addAll(Arrays.asList(id));
        }
        else
        {
            userIdSet.add(userIds.trim());
        }

        for (String key : SESSION_USERID.keySet())
        {
            if (SESSONID_WEBSOCKETSET_MAP.containsKey(key) && userIds.contains(SESSION_USERID.get(key)))
            {
                WebSocket webSocket = SESSONID_WEBSOCKETSET_MAP.get(key);
                webSocket.sendMessage(String.format("%s: %s", name, msg));
            }
        }
    }

    public void sendMessage(String message)
    {
        System.out.println("sendMessage 发送消息=" + message);
        try
        {
            this.session.getBasicRemote().sendText(message);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            LOGGER.error("发送消息失败：" + e.getMessage());
        }
    }

    public static synchronized int getOnLineCount()
    {
        return WebSocket.ON_LINE_COUNT;
    }

    public static synchronized void addOnlineCount()
    {
        System.out.println("addOnlineCount 在线数加1");
        WebSocket.ON_LINE_COUNT++;
    }

    public static synchronized void subOnlineCount()
    {
        System.out.println("subOnlineCount 在线数减1");
        WebSocket.ON_LINE_COUNT--;
    }

    /**
     * 发生错误时调用,监听错误事件
     */
    @OnError
    public void onError(Session session, Throwable thr)
    {
        System.out.println("onError 连接出错了");
        System.out.println(session.getId());
        thr.printStackTrace();
    }

    @Scheduled(cron = "*/30 * * * * ?")
    public void updateData()
    {
        LOGGER.info("清理 SESSONID_WEBSOCKETSET_MAP 详情:{}", SESSONID_WEBSOCKETSET_MAP.size());
        if (!SESSONID_WEBSOCKETSET_MAP.isEmpty())
        {
            Iterator<Map.Entry<String, WebSocket>> iterator = SESSONID_WEBSOCKETSET_MAP.entrySet().iterator();
            while (iterator.hasNext())
            {
                Map.Entry<String, WebSocket> next = iterator.next();
                if (!next.getValue().session.isOpen())
                {
                    String id = next.getValue().session.getId();
                    System.out.printf("清理无用的session ,sessionId={},userId={}", id, next.getKey());
                    LOGGER.info("清理无用的session ,sessionId={},userId={}", id, next.getKey());
                    iterator.remove();
                }
            }
        }

    }

    /**
     * 获取当前时间
     */
    private String getNowTime()
    {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(date);
        return time;
    }
}
