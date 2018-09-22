package eCare.configuration;

import eCare.configuration.camel.MessageProcessor;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by echerkas on 21.06.2018.
 */
@Component
public class MessageSender {

    @Autowired(required = false)
    private MessageProcessor messageProcessor;

    public void sendMessage(Exchange message) {
        messageProcessor.process(message);
    }
}
