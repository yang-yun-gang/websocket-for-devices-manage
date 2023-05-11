## 一，项目简介
该组件位于设备和平台之间，向下管理设备，向上接收平台对设备的操作，可提供信息发布，设备在离线检测功能。
## 二，项目流程图
![流程图](./picture/%E6%B5%81%E7%A8%8B%E5%9B%BE.png)
## 三，api
使用Postman调用测试
1. websocket连接
```
ws://127.0.0.1:8080/test/server/1001
```
2. 注册设备（连接后直接在ws里发送）
```
{
    "type": "signin",
    "data": {
        "deviceId": "1001",
        "deviceName": "1号机"
    }
}
```
3. ping（设备心跳）
```
{
    "type": "ping"
}
```
会收到组件发送的pong回复。

4. 获取待添加设备

get方法
```
http://127.0.0.1:8080/test/dsc/api/list
```
返回格式
```
[
    {
        "deviceId": "1001",
        "deviceName": "1号机"
    }
]
```
5. 删除待添加设备

post方法
```
http://localhost:8080/test/dsc/api/deleteAddDevices
```
消息体
```
["1", "2"]
```
6. 向设备推送信息（模拟平台发送的kafka消息）

post方法
```
http://localhost:8080/test/dsc/api/sendMsgByKafka
```
消息体
```
{
    "title": "重要通知",
    "content": "今晚七点开会",
    "deviceIds": ["1001", "admin", "kaka"]
}
```
