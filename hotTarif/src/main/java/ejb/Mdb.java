package ejb;

import ejb.rest.Rest;

import javax.ejb.*;
import javax.inject.Inject;
import javax.jms.*;
import java.util.logging.Logger;

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
        if (message instanceof TextMessage) {
            try {
                final String text = ((TextMessage) message).getText();
                LOGGER.info(() -> "Received: " + text);
                tarifBean.returnTarifFromQueue(text);
                rest.getTarifs();
            } catch (final JMSException e) {
                throw new RuntimeException(e);
            }
        }
    }
}


