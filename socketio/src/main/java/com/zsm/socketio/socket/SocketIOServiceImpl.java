package com.zsm.socketio.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @Author: zengsm.
 * @Description: TODO()
 * @Date:Created in 2020-03-13 10:57.
 * @Modified By:
 */
@Service(value = "socketIOService")
public class SocketIOServiceImpl implements SocketIOService
{
    // 用来存已连接的客户端
    private static Map<String, SocketIOClient> CLIENT_MAP = new ConcurrentHashMap<>();

    @Autowired
    private SocketIOServer socketIOServer;

    @Override
    public void start()
    {
        // 监听客户端连接
        socketIOServer.addConnectListener(client -> {
            String userId = getParamsByClient(client);
            if (userId != null)
            {
                System.out.println("SessionId:  " + client.getSessionId());
                System.out.println("RemoteAddress:  " + client.getRemoteAddress());
                System.out.println("Transport:  " + client.getTransport());
                System.out.println("用户已经连接：userId=" + userId);
                CLIENT_MAP.put(userId, client);
                pushMessage(new Message("connect", userId, "连接成功..."));
                socketIOServer.getBroadcastOperations().sendEvent("pushEvent", "connect广播发送消息" + userId);
            }
        });

        // 监听客户端断开连接
        socketIOServer.addDisconnectListener(client -> {
            String userId = getParamsByClient(client);
            if (userId != null)
            {
                System.out.println("用户已经断开连接：userId=" + userId);
                client.disconnect();
                CLIENT_MAP.remove(userId);
            }
        });

        /* 添加监听事件，监听客户端的事件
         * 1.第一个参数eventName需要与客户端的事件要一致
         * 2.第二个参数eventClass是传输的数据类型
         * 3.第三个参数listener是用于接收客户端传的数据，数据类型需要与eventClass一致
         */
        socketIOServer.addEventListener("pushEvent", String.class, (client, data, ackSender) -> {
            String userId = client.getHandshakeData().getSingleUrlParam("userId");
            System.out.println("push event do something...");
            System.out.println("用户发送消息：userId=" + userId + " message:" + data);
            pushMessage(new Message("pushEvent", userId, "发送消息" + data));
            socketIOServer.getBroadcastOperations().sendEvent("pushEvent", "广播发送消息" + data);
        });
        socketIOServer.start();
    }

    @Override
    public void stop()
    {
        if (socketIOServer != null)
        {
            socketIOServer.stop();
            socketIOServer = null;
        }
    }

    @Override
    public void pushMessage(Message message)
    {
        System.out.println("接收到消息：" + message.toString());
        String userId = message.getUserId();
        if (!StringUtils.isEmpty(userId))
        {
            SocketIOClient client = CLIENT_MAP.get(userId);
            if (client != null)
                client.sendEvent(PUSH_EVENT, message.toString());
        }
    }

    /**
     * 此方法为获取client连接中的参数，可根据需求更改
     */
    private String getParamsByClient(SocketIOClient client)
    {
        // 从请求的连接中拿出参数（这里的userId必须是唯一标识）
        Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
        List<String> list = params.get("userId");
        if (list != null && list.size() > 0)
        {
            return list.get(0);
        }
        return null;
    }

    /**
     * Spring IoC容器创建之后，在加载SocketIOServiceImpl Bean之后启动
     */
    @PostConstruct
    private void autoStartup()
    {
        start();
    }

    /**
     * Spring IoC容器在销毁SocketIOServiceImpl Bean之前关闭,避免重启项目服务端口占用问题
     */
    @PreDestroy
    private void autoStop()
    {
        stop();
    }
}
