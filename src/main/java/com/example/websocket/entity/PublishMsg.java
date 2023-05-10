package com.example.websocket.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description : 发送消息
 * @Author : young
 * @Date : 2023-05-08 17:17
 * @Version : 1.0
 **/
@Data
public class PublishMsg implements Serializable {

    private String title;

    private String content;

    private List<String> deviceIds;


}
