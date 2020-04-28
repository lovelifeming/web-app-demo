package com.zsm.socketinterceptor.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;


/**
 * @Author: zengsm.
 * @Description: TODO()
 * @Date:Created in 2020-04-20 16:54.
 * @Modified By:
 */
@Configuration
@EnableWebSocketMessageBroker
//@EnableWebSocketMessageBroker注解表示开启使用STOMP协议来传输基于代理的消息，Broker就是代理的意思。
public class WebSocketConfig implements WebSocketConfigurer
{
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry)
    {
        WebSocketNotificationHandler notificationHandler = new WebSocketNotificationHandler();

        webSocketHandlerRegistry.addHandler(notificationHandler, "/ws-notification") // (A)
            .addInterceptors(new HttpSessionHandshakeInterceptor())  // (B)
            .withSockJS();  // (C)
    }

    //@Override
    //public void configureMessageBroker(MessageBrokerRegistry config)
    //{
    //    config.enableSimpleBroker("/topic", "/user");//topic用来广播，user用来实现p2p
    //}
    //
    //@Override
    //public void registerStompEndpoints(StompEndpointRegistry registry)
    //{
    //    // registerStompEndpoints方法表示注册STOMP协议的节点，并指定映射的URL,注册STOMP协议节点同时指定使用SockJS协议。
    //    registry.addEndpoint("/webServer").withSockJS();
    //}
}
