package eCare.configuration.camel;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.UUID;

/**
 * Created by echerkas on 02.09.2018.
 */
@Component
public class MessageProcessor implements Processor {

    @Autowired
    private JmsTemplate jmsTemplate;

    public UUID createUuid() {
        UUID uuid = UUID.randomUUID();
        return uuid;
    }

    public void process(Exchange message){
        System.out.println("Got message: " + message);
        final UUID correlationID = createUuid();
        message.getIn().setHeader("JMSCorrelationID", correlationID);

        ProducerTemplate template = message.getContext().createProducerTemplate();
        Endpoint endpoint = message.getContext().getEndpoint("activemq:queue:TestQueue");
        template.setDefaultEndpoint(endpoint);

        try {
            template.start();
            template.sendBody(message.getIn().getBody());
            template.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }

        }

//        jmsTemplate.send(new MessageCreator(){
//            @Override
//            public javax.jms.Message createMessage(Session session) throws JMSException {
//                TextMessage textMessage = session.createTextMessage(message.getIn().getBody().toString());
//                System.out.println("Sending message " + message.toString());
//                textMessage.setJMSCorrelationID(message.getIn().getHeader("JMSCorrelationID").toString());
//                return textMessage;
//            }
//        });
//    }
//
//    public String String(String message){
//        return message;
//    }
    }

