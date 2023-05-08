package com.example.websocket.service;

import com.example.websocket.entity.DeviceInfo;

import java.util.List;

public interface DeviceService {

    /**
     *@Description : 保存/更新设备信息
     *@Author : young
     *@Date : 2022-07-26 16:58
     *@Version : 1.0
     **/
    void saveDeviceInfo(DeviceInfo deviceInfo);

    /**
      * @Description: 平台获取待添加列表
      * @Author: young
      * @Date: 2022-07-26 17:55
      * @return: java.util.List<com.example.websocket.entity.DeviceInfo>
      * @Version: 1.0
      **/
    List<DeviceInfo> getDevicesList();

    /**
     * @Description: 服务器调用接口删除待添加设备
     * @Author: young
     * @Date: 2023-05-07 12:17
     * @Param deviceIds: 删除的设备序列号列表
     * @return: 删除成功的设备序列号列表，当设备离线时，删除失败
     * @Version: 1.0
     **/
    List<String> deleteAddDevices(List<String> deviceIds);

    /**
      * @Description: 删除设备
      * @Author: young
      * @Date: 2022-09-14 16:38
      * @Param deviceIds:
      * @return: void
      * @Version: 1.0
      **/
    void deleteDevices(List<String> deviceIds);

    /**
      * @Description: 设备心跳更新
      * @Author: young
      * @Date: 2022-07-26 17:59
      * @Param deviceId:
      * @return: void
      * @Version: 1.0
      **/
    void heartbeatUpdate(String deviceId);

    /**
      * @Description: 发送日程
      * @Author: young
      * @Date: 2022-07-26 21:37
      * @Param deviceIds:
      * @Param scheduleId:
      * @return: void
      * @Version: 1.0
      **/
    void sendSchedule(String[] deviceIds, String scheduleId);

    /**
      * @Description: 设备离线处理
      * @Author: young
      * @Date: 2022-07-26 21:53
      * @Param deviceId:
      * @return: void
      * @Version: 1.0
      **/
    void dealDeviceOffline(String deviceId);
}
