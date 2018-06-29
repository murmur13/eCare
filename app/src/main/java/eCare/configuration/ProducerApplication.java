package eCare.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by echerkas on 21.06.2018.
 */
@Component
public class ProducerApplication {

    static final Logger LOG = LoggerFactory.getLogger(ProducerApplication.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    public void send(String destination, String message) {
        LOG.info("sending message='{}' to destination='{}'", message, destination);
        jmsTemplate.convertAndSend(destination, message);
    }
}
