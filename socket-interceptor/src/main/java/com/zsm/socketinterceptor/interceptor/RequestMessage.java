package com.zsm.socketinterceptor.interceptor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @Author: zeng.
 * @Date:Created in 2020-04-24 09:47.
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestMessage
{

    private String userId;

    private String sessionId;

    private String userName;

    private String message;
}
