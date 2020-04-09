package com.zsm.websocket.socket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;


/**
 * @Author: zengsm.
 * @Description: TODO()
 * @Date:Created in 2020-03-11 11:07.
 * @Modified By:
 */
@Configuration
public class WebSocketConfig
{
    @Bean
    public ServerEndpointExporter serverEndpointExporter()
    {
        return new ServerEndpointExporter();
    }
}
