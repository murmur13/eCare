package eCare.configuration;

import java.util.Arrays;

import javax.jms.ConnectionFactory;

import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.SimpleMessageConverter;

/**
 * Created by echerkas on 21.06.2018.
 */
@Configuration
public class MessagingConfiguration {

    private static final String DEFAULT_BROKER_URL = "tcp://localhost:61616";

    private static final String TEST_QUEUE = "TestQueue";

    @Bean
    public ConnectionFactory connectionFactory(){
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(DEFAULT_BROKER_URL);
        connectionFactory.setTrustedPackages(Arrays.asList("eCare.model.po.Tarif"));
        return connectionFactory;
    }
    /*
     * Optionally you can use cached connection factory if performance is a big concern.
     */

    @Bean
    public ConnectionFactory cachingConnectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setTargetConnectionFactory(connectionFactory());
        connectionFactory.setSessionCacheSize(10);
        return connectionFactory;
    }

    /*
     * Used for Sending Messages.
     */
    @Bean
    public JmsTemplate jmsTemplate(){
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(connectionFactory());
        template.setDefaultDestinationName(TEST_QUEUE);
        return template;
    }


    @Bean
    MessageConverter converter(){
        return new SimpleMessageConverter();
    }

}
