package com.org.demo.kafka.consumer;

import java.util.ArrayList;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.event.ListenerContainerIdleEvent;
import org.springframework.kafka.listener.MessageListenerContainer;
import com.ibm.jzos.ZFile;
import com.ibm.jzos.ZFileException;

@SpringBootApplication
public class Application {
	
	public Application() throws ZFileException {
		ZFile output = new ZFile("//DD:OUTPUT", "wb,type=record,recfm=fb,lrecl=200,noseek");
		output.close();
		ZFile error = new ZFile("//DD:ERROR", "wb,type=record,recfm=fb,lrecl=200,noseek");
		error.close();
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		StopKafkaTask stopKafka = new StopKafkaTask(25);
	}
	
	@EventListener
    public void onIdleEvent(ListenerContainerIdleEvent event) {
        System.out.println("Listener is idle for 2 mins, closing the application");
        ArrayList<MessageListenerContainer> containerList =  (ArrayList<MessageListenerContainer>) demoKafkaService.getListener();
        containerList.get(0).stop();
        System.exit(0);
    }
}
