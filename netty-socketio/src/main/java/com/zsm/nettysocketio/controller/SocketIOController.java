package com.zsm.nettysocketio.controller;

import com.alibaba.fastjson.JSONObject;
import com.zsm.nettysocketio.NettySocketioApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @Author: zengsm.
 * @Description: TODO()
 * @Date:Created in 2020-03-12 21:09.
 * @Modified By:
 */
@RestController
@RequestMapping("/socket")
public class SocketIOController
{
    @Value("${socketio.port}")
    private Integer port;

    @RequestMapping(value = "/test/{param}", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getApplicationInfo(@PathVariable("param") String param)
    {
        if ("shutdown".equals(param))
        {
            ExecutorService service = Executors.newSingleThreadExecutor();
            service.submit(() -> {
                try
                {
                    Thread.sleep(3);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                // 调用主线程，关闭整个程序
                NettySocketioApplication.exitApplication();
            });
        }
        JSONObject json = new JSONObject();
        json.put("名称:", "SocketIO测试控制器");
        json.put("版本:", "SocketIO测试控制器!@#$%^&*()_+");
        json.put("日期:", new Date());
        json.put("参数:", param);
        json.put("Socketio.port:", port);
        return json;
    }
}
