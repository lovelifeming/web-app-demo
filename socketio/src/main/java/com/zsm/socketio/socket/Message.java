package com.zsm.socketio.socket;

public class Message<T>
{
    private String type;

    private String userId;

    private T message;

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public T getMessage()
    {
        return message;
    }

    public void setMessage(T message)
    {
        this.message = message;
    }

    public Message(String type, String userId, T messages)
    {
        this.type = type;
        this.userId = userId;
        this.message = messages;
    }

    public Message()
    {

    }

    @Override
    public String toString()
    {
        return "Message{" +
               "type='" + type + '\'' +
               ", userId='" + userId + '\'' +
               ", message=" + message +
               '}';
    }
}