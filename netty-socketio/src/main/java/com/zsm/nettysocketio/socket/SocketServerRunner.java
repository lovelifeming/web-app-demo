package com.zsm.nettysocketio.socket;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Component
@Order()
public class SocketServerRunner implements CommandLineRunner
{

    @Autowired
    private SocketIOServer server;

    @Override
    public void run(String... args)
    {
        server.start();
    }
}