package com.example.websocket.thread;

import com.example.websocket.common.CommonDefine;
import com.example.websocket.service.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Description : 检查心跳线程
 * @Author : young
 * @Date : 2022-07-26 21:39
 * @Version : 1.0
 **/
@Component
public class CheckHeartBeatTask implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(CheckHeartBeatTask.class);

    public static ConcurrentHashMap<String, Long> heartbeatMap = new ConcurrentHashMap<>();

    @Autowired
    public DeviceService deviceService;

    // 实现启动服务时自启
    @PostConstruct
    public void init() {
        log.info("心跳机制检测线程启动");
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true) {
            // 弱一致性迭代器 在迭代器工作时后续加入的部分也能看见
            for (Map.Entry<String, Long> entry : heartbeatMap.entrySet()) {
                Date nowDate = new Date();
                long nowTime = nowDate.getTime();
                Long lastUpdate = entry.getValue();
                if (nowTime - lastUpdate > CommonDefine.Heartbeat.TIME) {
                    //30s未更新，判定离线
                    String deviceId = entry.getKey();
                    log.info("设备{}离线", deviceId);
                    heartbeatMap.remove(entry.getKey());
                    deviceService.dealDeviceOffline(deviceId);
                }

            }

            try {
                TimeUnit.MILLISECONDS.sleep(CommonDefine.Heartbeat.TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
