package com.zsm.nettysocketio.socket;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;


@org.springframework.context.annotation.Configuration
public class SocketIOConfig
{
    @Value("${socketio.host}")
    private String host;

    @Value("${socketio.port}")
    private Integer port;

    @Value("${socketio.bossCount}")
    private int bossCount;

    @Value("${socketio.workCount}")
    private int workCount;

    @Value("${socketio.allowCustomRequests}")
    private boolean allowCustomRequests;

    @Value("${socketio.upgradeTimeout}")
    private int upgradeTimeout;

    @Value("${socketio.pingTimeout}")
    private int pingTimeout;

    @Value("${socketio.pingInterval}")
    private int pingInterval;

    @Bean
    public SocketIOServer socketIOServer()
    {
        Configuration config = new Configuration();
        config.setHostname(host);        // 设置主机名
        config.setPort(port);       // 设置监听端口
        // 协议升级超时时间（毫秒），默认10000。HTTP握手升级为ws协议超时时间
        config.setUpgradeTimeout(upgradeTimeout);
        // Ping消息间隔（毫秒），默认25000。客户端向服务器发送一条心跳消息间隔
        config.setPingInterval(pingInterval);
        // Ping消息超时时间（毫秒），默认60000，这个时间间隔内没有接收到心跳消息就会发送超时事件
        config.setPingTimeout(pingTimeout);

        // 握手协议参数使用JWT的Token认证方案
        //config.setAuthorizationListener(data -> {
        //  //可以使用如下代码获取用户密码信息进行权限校验
        //String username = data.getSingleUrlParam("username");
        //String password = data.getSingleUrlParam("password");
        //  String token = data.getSingleUrlParam("token");
        //  return true;
        //});

        SocketIOServer ioServer = new SocketIOServer(config);
        ioServer.addNamespace("zsm");
        return ioServer;
    }

    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer)
    {
        return new SpringAnnotationScanner(socketServer);
    }
}