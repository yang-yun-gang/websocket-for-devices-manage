package com.example.websocket.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @Description : 处理发布日程线程
 * @Author : young
 * @Date : 2022-07-28 0:23
 * @Version : 1.0
 **/
public class DealScheduleTask implements Runnable{

    public static BlockingQueue<Object> scheduleBlockingQueue = new LinkedBlockingDeque<>(1000);

    @Override
    public void run() {
        while (true) {
            try {
                Object task = scheduleBlockingQueue.take();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
