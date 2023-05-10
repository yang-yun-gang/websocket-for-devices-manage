package com.example.websocket.kafka;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.websocket.common.CommonDefine;
import com.example.websocket.entity.PublishMsg;
import com.example.websocket.service.DeviceService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @Description : 消费者
 * @Author : young
 * @Date : 2023-05-07 12:11
 * @Version : 1.0
 **/
@Component
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired
    private DeviceService deviceService;

    // 监听信息发布
    @KafkaListener(topics = "topic.publish", groupId = CommonDefine.KafkaParams.GROUP_DEFAULT)
    public void test(ConsumerRecord<?, ?> record, Acknowledgment ack) {
        Optional message = Optional.ofNullable(record.value());
        if (message.isPresent()) {
            JSONObject jsonObject = JSON.parseObject(message.get().toString());
            PublishMsg publishMsg = jsonObject.toJavaObject(PublishMsg.class);
            log.info("kafka收到消息： Topic:" + record.topic() + ",Message:" + publishMsg);
            // 消费消息
            ack.acknowledge();
            deviceService.publish(publishMsg);
        }
    }

}
