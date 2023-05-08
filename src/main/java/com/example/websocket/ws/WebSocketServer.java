package com.example.websocket.ws;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.websocket.common.CommonDefine;
import com.example.websocket.entity.DeviceInfo;
import com.example.websocket.entity.Response;
import com.example.websocket.service.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description : websocket服务
 * @Author : young
 * @Date : 2022-07-22 23:22
 * @Version : 1.0
 **/
@ServerEndpoint("/server/{deviceId}")
@Component
public class WebSocketServer {

    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);

    // 存储设备唯一标识与ws
    private static ConcurrentHashMap<String, WebSocketServer> map = new ConcurrentHashMap<>();

    private static DeviceService deviceService;

    private Session session;

    private String deviceId = "";

    static {
        log.info("websocket初始化");
    }

    @Autowired
    public void setDeviceService(DeviceService deviceService) {
        WebSocketServer.deviceService = deviceService;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("deviceId") String deviceId) {
        this.session = session;
        this.deviceId = deviceId;

        map.put(deviceId, this);

        log.info("设备" + deviceId + "连接，当前在线的设备数为" + map.size());

    }

    @OnClose
    public void onClose() {
        map.remove(deviceId);
        log.info("设备" + deviceId + "连接关闭，当前在线的设备数为" + map.size());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到来自会话{}的消息{}", session, message);

        // 判断消息类型
        if(Objects.nonNull(message)){
            try {
                // 解析报文
                JSONObject jsonObject = JSON.parseObject(message);
                String type = jsonObject.getString("type");

                switch (type) {
                    // 注册
                    case CommonDefine.Type.SIGNIN:
                        DeviceInfo deviceInfo = jsonObject.getObject("data", DeviceInfo.class);
                        deviceService.saveDeviceInfo(deviceInfo);
                        break;
                    // 心跳
                    case CommonDefine.Type.PING:
                        deviceService.heartbeatUpdate(deviceId);
                        Response response = new Response(CommonDefine.Type.PONG, null);
                        String responseString = JSON.toJSONString(response);
                        sendMsg(responseString);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("设备错误:"+this.deviceId+",原因:"+error.getMessage());
        error.printStackTrace();
    }

    // 服务器发送消息
    public void sendMsg(String message) throws IOException {
        log.info("发送消息到:" + deviceId + ",消息内容:" + message);
        this.session.getBasicRemote().sendText(message);
    }

    // 发送日程
    public static void sendSchedule(String scheduleId, String[] deviceIds){
        for (String deviceId : deviceIds) {
            if (map.containsKey(deviceId)) {
                // todo 有没有可能进入的时候该设备下线了
                WebSocketServer webSocketServer = map.get(deviceId);
                Response response = new Response(CommonDefine.Type.SCHEDULE, scheduleId);
                String responseString = JSON.toJSONString(response);
                try {
                    webSocketServer.sendMsg(responseString);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                log.error("设备{}不在线，无法发送消息", deviceId);
            }
        }
    }

    // 设备掉线处理方法 关闭连接
    public static void closeConn(String deviceId) {
        if (map.containsKey(deviceId)) {
            WebSocketServer webSocketServer = map.get(deviceId);
            Session session = webSocketServer.session;
            try {
                // 关闭设备离线的连接
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
