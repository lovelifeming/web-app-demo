package com.zsm.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import java.util.Scanner;


@SpringBootApplication(scanBasePackages = "com.zsm.websocket")
//@PropertySource(value = {"classpath:application.properties"},encoding="UTF-8")
public class WebsocketApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(WebsocketApplication.class, args);
    }

}
