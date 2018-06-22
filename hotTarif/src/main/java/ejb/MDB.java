package ejb;


import org.apache.activemq.spring.ActiveMQConnectionFactory;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.*;
import java.util.logging.Logger;

/**
 * Created by echerkas on 21.06.2018.
 */
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "queue/TestQueue"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
})
public class MDB implements MessageListener {

    private final static Logger LOGGER = Logger.getLogger(MDB.class.toString());

    private static final String DEFAULT_BROKER_URL = "tcp://localhost:61616";

    public MDB() throws JMSException {
    }

//    // Create a ConnectionFactory
//    ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
//
//    // Create a Connection
//    Connection connection = connectionFactory.createConnection();
//                connection.start();
//
//                connection.setExceptionListener(this);
//
//    // Create a Session
//    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//
//    // Create the destination (Topic or Queue)
//    Destination destination = session.createQueue("TestQueue");
//
//    // Create a MessageConsumer from the Session to the Topic or Queue
//    MessageConsumer consumer = session.createConsumer(destination);
//
//    // Wait for a message
//    Message message = consumer.receive(1000);

    @Override
    public void onMessage(Message message) {
//        TextMessage msg = null;
        try {
            ObjectMessage objectMessage = (ObjectMessage) message;
//            if (message instanceof TextMessage) {
//                msg = (TextMessage) message;
            LOGGER.info("Received Message from queue: " + objectMessage.getObject().toString());
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }


//                consumer.close();
//                session.close();
//                connection.close();
//} catch (Exception e) {
//        System.out.println("Caught: " + e);
//        e.printStackTrace();
//        }
//
//public synchronized void onException(JMSException ex) {
//        System.out.println("JMS Exception occured.  Shutting down client.");
//        }
//        }

}
