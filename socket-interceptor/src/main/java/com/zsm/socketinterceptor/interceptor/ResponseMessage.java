package com.zsm.socketinterceptor.interceptor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * @Author: zeng.
 * @Date:Created in 2020-04-24 09:47.
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMessage
{

    private String sender;

    private String sessionId;

    private Date sendTime;

    private String message;

}
