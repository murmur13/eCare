package eCare.configuration;

import eCare.configuration.camel.IdempotentConsumer;
import eCare.configuration.camel.MessageProcessor;
import eCare.model.po.Tarif;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.ExpressionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.UUID;

/**
 * Created by echerkas on 21.06.2018.
 */
@Component
public class MessageSender {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired(required=false)
    private MessageProcessor messageProcessor;

    private TextMessage msg;

    public UUID createUuid(){
        UUID uuid = UUID.randomUUID();
        return uuid;
    }

    public void sendMessage(Exchange message) {
        messageProcessor.process(message);

//        jmsTemplate.send(new MessageCreator(){
//            @Override
//            public Message createMessage(Session session) throws JMSException {
//                TextMessage textMessage = session.createTextMessage(message.toString());
//                System.out.println("Sending message " + message.toString());
//                textMessage.setJMSCorrelationID(createUuid().toString());
//                msg = textMessage;
//                return textMessage;
//            }
//        });
//        jmsTemplate.send(new MessageCreator(){
//            @Override
//            public Message createMessage(Session session) throws JMSException {
//                TextMessage textMessage = msg;
//                msg = textMessage;
//                return textMessage;
//            }
//        });

    }

}
