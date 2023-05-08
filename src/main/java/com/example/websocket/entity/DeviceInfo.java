package com.example.websocket.entity;

import lombok.Data;

/**
 * @Description : 设备信息
 * @Author : young
 * @Date : 2022-07-26 16:59
 * @Version : 1.0
 **/
@Data
public class DeviceInfo {

    // 设备唯一标识
    private String deviceId;

    // 设备名字
    private String deviceName;


}
