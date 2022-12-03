package com.org.demo.kafka.consumer;

import java.util.ArrayList;
import java.util.TimerTask;

import org.springframework.kafka.listener.MessageListenerContainer;

public class KafkaStopper extends TimerTask{
	public void run() { 
		 System.out.println("Timer Task Initiated");
		 System.out.println("Maximum Job execution time limit reached, closing the application");
		 ArrayList<MessageListenerContainer> containerList =  (ArrayList<MessageListenerContainer>) demoKafkaService.getListener();
		 containerList.get(0).stop();
		 System.exit(0);
	 }
}
