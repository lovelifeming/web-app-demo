package com.zsm.socketio.socket;

/**
 * @Author: zengsm.
 * @Description: TODO()
 * @Date:Created in 2020-03-13 10:55.
 * @Modified By:
 */
public interface SocketIOService
{
    //推送的事件
    String PUSH_EVENT = "pushEvent";

    // 启动服务
    void start();

    // 停止服务
    void stop();

    // 推送信息
    void pushMessage(Message message);
}
