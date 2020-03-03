package com.zsm.websocket.socket;//package com.digitized.server.websocket;
//
//import com.alibaba.fastjson.JSONObject;
//import com.digitized.server.util.CommonUtil;
//import io.swagger.annotations.*;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.websocket.*;
//import javax.websocket.server.ServerEndpoint;
//import java.io.IOException;
//import java.util.*;
//import java.util.concurrent.CopyOnWriteArraySet;
//import java.util.concurrent.Executor;
//import java.util.concurrent.Executors;
//
//
///**
// * @Author :zengsm.
// * @Description :
// * @Date:Created in 2019/5/27 16:38.
// * @Modified By : {sessionId}/
// */
//@Api
//@SwaggerDefinition(tags = @Tag(name = "设备劣化趋势 数据监控", description = "设备劣化趋势 图表数据websocket"))
//@ServerEndpoint("/digitized/equipmentWs")
//@RestController
//public class EquipmentWebSocket
//{
//    // 用来记录当前连接数的变量
//    private static volatile int onlineCount = 0;
//
//    // concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象
//    private static CopyOnWriteArraySet<EquipmentWebSocket> WEB_SOCKETSET = new CopyOnWriteArraySet<EquipmentWebSocket>();
//
//    // 与某个客户端的连接会话，需要通过它来与客户端进行数据收发
//    private Session session;
//
//    private String equipment_name = "";
//
//    public volatile boolean runing = false;
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentWebSocket.class);
//
//    private static final Executor CACHE_THREAD_POOL = Executors.newCachedThreadPool();
//
//    @ApiOperation("设备劣化趋势 发送设备名 send(message) ")
//    @ApiImplicitParams({@ApiImplicitParam(name = "message", value = "设备名代码", example = "Motor_Fan_QAH")})
//    @RequestMapping(value = "message", method = RequestMethod.GET)
//    @OnMessage
//    public void onMessage(String message, Session session)
//    {
//        LOGGER.info("Receive a message from client: " + message);   //  Motor_Fan_QAH
//
//        /**
//         * 1.传递空字符
//         * 2.第一次进入，传入正常设备名
//         * 3.第二次进入，传入相同设备名
//         * 4.第三次进入，传入不同设备名
//         */
//        if (StringUtils.isEmpty(message))
//        {
//            runing = false;
//            return;
//        }
//        else
//        {
//            runing = false;
//            equipment_name = message;
//            runSendMessage(message);
//        }
//    }
//
//    private void runSendMessage(String message)
//    {
//        //KafkaConsumer consumer = new KafkaConsumer("test", this);
//        runing = true;
//        //CACHE_THREAD_POOL.execute(consumer);
//        Random random = new Random();
//        while (runing)
//        {
//            String date = CommonUtil.getNowFormatDate();
//            JSONObject json = new JSONObject();
//            List<Map<String, String>> mapList = new ArrayList<>();
//            HashMap map1 = new HashMap();
//            map1.put("time", date);
//            map1.put("value", random.nextInt(10));
//            mapList.add(map1);
//
//            if(flag)
//            {
//                HashMap map2 = new HashMap();
//                map1.put("time", date);
//                map1.put("value", random.nextInt(10));
//                mapList.add(map2);
//
//                HashMap map3 = new HashMap();
//                map1.put("time", date);
//                map1.put("value", random.nextInt(10));
//                mapList.add(map3);
//
//                HashMap map4 = new HashMap();
//                map1.put("time", date);
//                map1.put("value", random.nextInt(10));
//                mapList.add(map4);
//            }
//            json.put("data", mapList);
//            ioClient.sendEvent("receiveMsg", json);
//            try
//            {
//                Thread.sleep(1000);
//            }
//            catch (InterruptedException e)
//            {
//                e.printStackTrace();
//            }
//        }
//
//        /**CACHE_THREAD_POOL.execute(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                LOGGER.info("Thread start ...");
//                Random random = new Random();
//                if (message.equals("Motor_Fan_QAH"))
//                {
//                    while (runing)
//                    {
//                        try
//                        {
//                            Thread.sleep(1000);
//                        }
//                        catch (InterruptedException e)
//                        {
//                            e.printStackTrace();
//                            LOGGER.error("internal process error:{}+{} ", Thread.currentThread().getId(), e);
//                        }
//                        int data = random.nextInt(10);
//                        //monitor.getMessage()
//                        sendMessage(session, String.valueOf(data));
//                    }
//                }
//                else
//                {
//                    while (runing)
//                    {
//                        try
//                        {
//                            Thread.sleep(1000);
//                        }
//                        catch (InterruptedException e)
//                        {
//                            e.printStackTrace();
//                            LOGGER.error("internal process error:{}+{} ", Thread.currentThread().getId(), e);
//                        }
//                        int data = random.nextInt(100);
//                        sendMessage(session, String.valueOf(data));
//                    }
//                }
//                LOGGER.info("Thread end ...");
//            }
//        });*/
//    }
//
//    @OnOpen
//    public void onOpen(Session session)
//        throws Exception
//    {
//        addOnlineCount();
//        this.session = session;
//        WEB_SOCKETSET.add(this);
//        LOGGER.info("Open a websocket. sessonId={}", session.getId());
//    }
//
//    @OnClose
//    public void onClose(Session session)
//    {
//        try
//        {
//            runing = false;
//            session.close();
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//            LOGGER.error("onClose IOException", e);
//        }
//        WEB_SOCKETSET.remove(this);
//        subOnlineCount();
//        LOGGER.info("Close a websocket. " + session.getId());
//    }
//
//    @OnError
//    public void onError(Session session, Throwable error)
//    {
//        try
//        {
//            runing = false;
//            session.close();
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//            LOGGER.error("onError IOException,{}", e);
//        }
//        LOGGER.error("Error while websocket. ", error);
//    }
//
//    public void sendMessage(String message)
//    {
//        if (this.session.isOpen())
//        {
//            try
//            {
//                this.session.getBasicRemote().sendText(message);
//            }
//            catch (IOException e)
//            {
//                e.printStackTrace();
//                LOGGER.error("sendMessage IOException ", e);
//            }
//        }
//    }
//
//    public void sendMessage(Session session, String message)
//    {
//        if (session == null)
//        {
//            return;
//        }
//        final RemoteEndpoint.Basic basic = session.getBasicRemote();
//        if (basic == null)
//        {
//            LOGGER.error("sendMessage basic is nullpoit");
//            return;
//        }
//        try
//        {
//            basic.sendText(message);
//        }
//        catch (IOException e)
//        {
//            LOGGER.error("static sendMessage IOException ", e);
//        }
//    }
//
//    public static synchronized int getOnlineCount()
//    {
//        return onlineCount;
//    }
//
//    public static synchronized void addOnlineCount()
//    {
//        EquipmentWebSocket.onlineCount++;
//    }
//
//    public static synchronized void subOnlineCount()
//    {
//        EquipmentWebSocket.onlineCount--;
//    }
//}
