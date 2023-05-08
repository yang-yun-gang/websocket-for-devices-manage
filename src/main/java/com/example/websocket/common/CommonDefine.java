package com.example.websocket.common;

/**
 * @Description : TODO
 * @Author : young
 * @Date : 2022-07-26 21:44
 * @Version : 1.0
 **/
public class CommonDefine {

    public static class Type {
        // 注册类型
        public static final String SIGNIN = "signin";

        // ping
        public static final String PING = "ping";

        // pong
        public static final String PONG = "pong";

        // schedule
        public static final String SCHEDULE = "schedule";
    }

    public static class Heartbeat {
        // 心跳时间
        public static final long TIME = 30000;
    }

    public static class KafkaTopics{

        public static final String TOPIC_SCHEDULE = "topic.schedule";

        public static final String TOPIC_DEVICE_OFFLINE = "topic.device.offline";
    }

}
