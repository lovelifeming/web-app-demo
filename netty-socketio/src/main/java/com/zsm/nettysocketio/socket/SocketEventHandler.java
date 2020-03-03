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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@Component
public class SocketEventHandler
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketEventHandler.class);

    private Map<String, SocketIOClient> socketMap = new HashMap<>();

    private Logger logger = LoggerFactory.getLogger(SocketEventHandler.class);

    @Autowired
    private SocketIOServer server;

    //添加connect事件，当客户端发起连接时调用
    @OnConnect
    public void onConnect(SocketIOClient client)
    {
        if (client != null)
        {
            String name = client.getHandshakeData().getSingleUrlParam("username");
            //String imei = client.getHandshakeData().getSingleUrlParam("imei");
            //String appid = client.getHandshakeData().getSingleUrlParam("appid");
            //logger.info("用户设备码：{}, sessionId: {}", imei, client.getSessionId().toString());
            socketMap.put(name, client);
        }
        else
        {
            LOGGER.error("socket-io client is empty.");
        }
    }

    //添加@OnDisconnect事件，客户端断开连接时调用，刷新客户端信息
    @OnDisconnect
    public void onDisConnect(SocketIOClient client)
    {
        //String imei = client.getHandshakeData().getSingleUrlParam("imei");
        String[] username = new String[1];
        socketMap.forEach((key, value) -> {
            if (value == client)
                username[0] = key;
        });
        logger.info("用户设备码：{}断开连接", username[0]);
        socketMap.remove(username[0]);
        client.disconnect();
    }
    //@OnMessage
    //public void onMessage(SocketIOClient client,String message)
    //{
    //    System.out.println(message);
    //}

    // 自定义一个消息接收事件，用于接收客户端消息，参数类型必须一致，否则后端接收不到数据
    @OnEvent(value = "receiveMsg")
    public void onReceiveMsg(SocketIOClient client, AckRequest ackRequest, String message)
    {
        String username = client.getHandshakeData().getSingleUrlParam("username");
        System.out.println(message);
        Random random = new Random();
        for (int i = 0; ; i++)
        {
            JSONObject json = new JSONObject();
            json.put("time", new Date().toString());
            json.put("value", random.nextInt(10));
            socketMap.get(username).sendEvent("receiveMsg", json);
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

    //客户端上报用户信息
    @OnEvent(value = "doReport")
    public void onDoReport(SocketIOClient client, AckRequest ackRequest, Message param)
    {
        logger.info("报告地址接口 start....");
        ackRequest.sendAckData(param);
    }

}