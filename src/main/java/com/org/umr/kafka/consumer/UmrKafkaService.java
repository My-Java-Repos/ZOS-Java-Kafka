package com.org.demo.kafka.consumer;

import java.util.ArrayList;
import java.util.List;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Service;

import com.org.demo.kafka.consumer.demoKafkaService;

@Service
public class demoKafkaService {
	
	//private static final Logger LOGGER = LogManager.getLogger(demoKafkaService.class);
    private static final String TOPIC = "${demo.kafka.topic}";
    private static KafkaListenerEndpointRegistry registry;
    
    public demoKafkaService(KafkaListenerEndpointRegistry registry) {
        demoKafkaService.registry = registry;
    }
    
    public static List<MessageListenerContainer> getListener() {
        return new ArrayList<>(registry.getListenerContainers());
    }
    
    @KafkaListener(topics = TOPIC)
	public void consume(String message) {
    	ProcessResponse.formatResponse(message);
	}
}
