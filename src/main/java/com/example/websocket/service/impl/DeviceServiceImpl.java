package com.example.websocket.service.impl;

import com.example.websocket.common.CommonDefine;
import com.example.websocket.entity.DeviceInfo;
import com.example.websocket.kafka.KafkaProducer;
import com.example.websocket.service.DeviceService;
import com.example.websocket.thread.CheckHeartBeatTask;
import com.example.websocket.ws.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description : 设备服务实现类
 * @Author : young
 * @Date : 2022-07-26 17:03
 * @Version : 1.0
 **/
@Service
public class DeviceServiceImpl implements DeviceService {

    private static ConcurrentHashMap<String, DeviceInfo> deviceInfoMap = new ConcurrentHashMap<>();

    @Autowired
    private KafkaProducer kafkaProducer;

    @Override
    public void saveDeviceInfo(DeviceInfo deviceInfo) {
        deviceInfoMap.put(deviceInfo.getDeviceId(), deviceInfo);

    }

    @Override
    public List<DeviceInfo> getDevicesList() {
        List<DeviceInfo> deviceInfoList = new ArrayList<>();
        // todo 弱一致性 ConcurrentHashMap允许一边遍历一边更新
        for (Map.Entry<String, DeviceInfo> entry : deviceInfoMap.entrySet()) {
            deviceInfoList.add(entry.getValue());
        }

        return deviceInfoList;
    }

    @Override
    public List<String> deleteAddDevices(List<String> deviceIds) {

        List<String> deleteDeviceIds = new ArrayList<>();
        for (String deviceId : deviceIds) {
            //如果没有包含此设备 说明在添加时设备离线
            DeviceInfo deviceInfo = deviceInfoMap.remove(deviceId);
            if (Objects.nonNull(deviceInfo)) {
                deleteDeviceIds.add(deviceId);
            }
        }

        return deleteDeviceIds;
    }

    @Override
    public void deleteDevices(List<String> deviceIds) {
        for (String deviceId : deviceIds) {
            // 移除心跳
            CheckHeartBeatTask.heartbeatMap.remove(deviceId);
            // 关闭连接
            WebSocketServer.closeConn(deviceId);
        }
    }

    @Override
    public void heartbeatUpdate(String deviceId) {
        // 获取当前时间
        Date date = new Date();
        CheckHeartBeatTask.heartbeatMap.put(deviceId, date.getTime());
    }

    @Override
    public void sendSchedule(String[] deviceIds, String scheduleId) {
        WebSocketServer.sendSchedule(scheduleId, deviceIds);
    }

    @Override
    public void dealDeviceOffline(String deviceId) {
        DeviceInfo device = deviceInfoMap.remove(deviceId);
        // 通过kafka 通知平台设备离线
        if (Objects.nonNull(device)) {
            kafkaProducer.send(CommonDefine.KafkaTopics.TOPIC_DEVICE_OFFLINE, device.getDeviceId());
        }
        WebSocketServer.closeConn(deviceId);
    }


}
