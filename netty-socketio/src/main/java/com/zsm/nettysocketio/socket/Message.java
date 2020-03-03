package com.zsm.nettysocketio.socket;

import java.util.Map;

public class Message
{
    private String type;

    private Map<String, Object> message;

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public Map<String, Object> getMessage()
    {
        return message;
    }

    public void setMessage(Map<String, Object> message)
    {
        this.message = message;
    }

    public Message(String type, Map<String, Object> messages)
    {
        this.type = type;
        this.message = messages;
    }

    @Override
    public String toString()
    {
        return "Message{" +
               "type='" + type + '\'' +
               ", message=" + message +
               '}';
    }
}