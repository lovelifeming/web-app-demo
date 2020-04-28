package com.zsm.socketinterceptor.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * @Author: zeng.
 * @Date:Created in 2020-04-24 09:48.
 * @Description:
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer
{
    /**
     * 配置viewController,提供映射路径
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry)
    {
        registry.addViewController("/ws").setViewName("/ws");
        registry.addViewController("/webSocket").setViewName("/webSocket");
    }

}
