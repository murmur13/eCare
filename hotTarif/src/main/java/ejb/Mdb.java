package ejb;

import ejb.rest.Rest;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.security.util.FieldUtils;

import javax.ejb.*;
import javax.inject.Inject;
import javax.jms.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.apache.camel.model.rest.RestParamType.body;

/**
 * Created by echerkas on 21.06.2018.
 */
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "queue/TestQueue"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName="destination", propertyValue="TestQueue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
}, mappedName = "TestQueue")


public class Mdb implements MessageListener {

    private final static Logger LOGGER = Logger.getLogger(Mdb.class.toString());

    private static final String DEFAULT_BROKER_URL = "tcp://localhost:61616";

    public Mdb(){
    }

   @Inject
    TarifBean tarifBean;

    @Inject
    Rest rest;

    @Override
    public void onMessage(Message message){
            ObjectMessage objMessage = (ObjectMessage) message;
        try {
            Integer id = (Integer) objMessage.getObject();
            LOGGER.info("received: " + objMessage);
            tarifBean.returnTarifFromQueue(message.toString());
            rest.getTarifs();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        }
    }

