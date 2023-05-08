package com.example.websocket.entity;

import lombok.Data;

/**
 * @Description : 响应消息体
 * @Author : young
 * @Date : 2022-07-26 18:13
 * @Version : 1.0
 **/
@Data
public class Response {

    private String type;

    private Object data;

    public Response(String type, Object data) {
        this.type = type;
        this.data = data;
    }
}
