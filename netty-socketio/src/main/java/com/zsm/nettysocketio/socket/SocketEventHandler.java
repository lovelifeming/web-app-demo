package com.zsm.nettysocketio.socket;

import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.OnMessage;
import java.util.*;


@Component
public class SocketEventHandler
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketEventHandler.class);

    private Map<String, SocketIOClient> SOCKET_CLIENT_MAP = new HashMap<>();

    @Autowired
    private SocketIOServer server;

    //添加connect事件，当客户端发起连接时调用
    @OnConnect
    public void onConnect(SocketIOClient client)
    {
        if (client != null)
        {
            String name = client.getHandshakeData().getSingleUrlParam("username");
            String imei = client.getHandshakeData().getSingleUrlParam("imei");
            String appId = client.getHandshakeData().getSingleUrlParam("appid");
            Map<String, List<String>> urlParams = client.getHandshakeData().getUrlParams();
            urlParams.forEach((k, v) -> System.out.printf("urlParams参数：key:%s, value:%s", k, v));
            LOGGER.info("用户设备码：{}, sessionId: {},用户名：{},AppId：{}", imei, client.getSessionId().toString(),
                name, appId);
            SOCKET_CLIENT_MAP.put(appId, client);
        }
        else
        {
            LOGGER.error("socket-io client is empty.");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("message", "连接客户端成功！");
        client.sendEvent("receiveMsg", map);
    }

    // 添加@OnDisconnect事件，客户端断开连接时调用，刷新客户端信息
    @OnDisconnect
    public void onDisConnect(SocketIOClient client)
    {
        String appId = client.getHandshakeData().getSingleUrlParam("appid");
        if (!StringUtils.isEmpty(appId))
        {
            SOCKET_CLIENT_MAP.remove(appId);
            LOGGER.info("用户AppId：{}断开连接", appId);
        }
        else
        {
            String[] temp = new String[1];
            SOCKET_CLIENT_MAP.forEach((key, value) -> {
                if (value == client)
                    temp[0] = key;
            });
            LOGGER.info("用户AppId：{}断开连接", temp[0]);
            SOCKET_CLIENT_MAP.remove(temp[0]);
        }
        client.disconnect();
    }

    // 接收客户端消息，没有事件标识 onmessage
    @OnMessage
    public void onMessage(SocketIOClient client, String message)
    {
        System.out.println(message);
        client.sendEvent("receiveMsg", "onMessage 接收到消息");
    }

    // 自定义一个消息接收事件，用于接收客户端消息，参数类型必须一致，否则后端接收不到数据
    @OnEvent(value = "receiveMsg")
    public void onReceiveMsg(SocketIOClient client, AckRequest ackRequest, Message message)
    {
        System.out.println(message);
        System.out.println(ackRequest);
        Random random = new Random();
        String appId = client.getHandshakeData().getSingleUrlParam("appid");
        for (int i = 0; i < 5; i++)
        {
            JSONObject json = new JSONObject();
            json.put("appId", appId);
            json.put("time", new Date().toString());
            json.put("value", random.nextInt(10));
            SOCKET_CLIENT_MAP.get(appId).sendEvent("receiveMsg", json);
        }
    }

    // 客户端上报用户信息
    @OnEvent(value = "doReport")
    public void onDoReport(SocketIOClient client, AckRequest ackRequest, Message param)
    {
        LOGGER.info("报告地址接口 start....");
        ackRequest.sendAckData(param);
    }

}