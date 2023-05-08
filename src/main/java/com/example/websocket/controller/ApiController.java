package com.example.websocket.controller;

import com.example.websocket.entity.DeviceInfo;
import com.example.websocket.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description : 设备服务组件api接口
 * @Author : young
 * @Date : 2022-07-26 17:36
 * @Version : 1.0
 **/
@RestController
@RequestMapping("/dsc/api")
public class ApiController {

    @Autowired
    private DeviceService deviceService;

    // 服务调用接口获取待添加设备 这里应该返回的是标准json
    @GetMapping("/list")
    public List<DeviceInfo> getDevices() {
        return deviceService.getDevicesList();
    }

    // 服务器调用接口发送日程
    @GetMapping("/sendSchedule")
    public void sendSchedule() {

    }

    // 服务器调用接口删除设备
    @PostMapping(value = "/deleteDevices")
    public void deleteDevices(@RequestBody List<String> deviceIds) {
        deviceService.deleteDevices(deviceIds);
    }

    // 服务器添加设备时，先调用接口删除内存里的设备，在添加至数据库
    @PostMapping(value = "/deleteAddDevices")
    public List<String> deleteAddDevices(@RequestBody List<String> deviceIds) {
        return deviceService.deleteAddDevices(deviceIds);
    }
}
