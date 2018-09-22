package eCare.configuration.camel;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by echerkas on 02.09.2018.
 */
@Component
public class MessageProcessor implements Processor {

    public void process(Exchange message){
        System.out.println("Got message: " + message);

        ProducerTemplate template = message.getContext().createProducerTemplate();
        Endpoint endpoint = message.getContext().getEndpoint("activemq:queue:TestQueue");
        template.setDefaultEndpoint(endpoint);

        try {
            template.start();
            template.send(message);
            template.send(message);
            template.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        }
    }

