package com.org.demo.kafka.consumer;

import java.util.Timer;

public class StopKafkaTask {
	Timer timer;
	public StopKafkaTask(int minutes) {
        timer = new Timer();
        timer.schedule(new KafkaStopper(), minutes*60000);
    }
}
