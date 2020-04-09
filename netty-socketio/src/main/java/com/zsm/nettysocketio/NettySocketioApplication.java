package com.zsm.nettysocketio;

import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class NettySocketioApplication
{
    private static ConfigurableApplicationContext APPLICATION_CONTEXT;

    public static void main(String[] args)
    {
        APPLICATION_CONTEXT = SpringApplication.run(NettySocketioApplication.class, args);
    }

    public static void exitApplication()
    {
        // 注销应用上下文环境，关闭主程序
        int exitCode = SpringApplication.exit(APPLICATION_CONTEXT, (ExitCodeGenerator)() -> {
            System.out.println("SpringApplication.exit ExitCodeGenerator Springboot正在准备关闭... ");
            return 0;
        });
        System.out.println("exitCode:" + exitCode);
        System.exit(exitCode);
    }
}
