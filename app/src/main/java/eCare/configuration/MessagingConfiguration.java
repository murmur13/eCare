package eCare.configuration;

import javax.jms.ConnectionFactory;

import eCare.configuration.camel.JmsToEmailRoute;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
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
    public CamelContext camelContext() {
        return new DefaultCamelContext();
    }

    @Bean
    public ConnectionFactory connectionFactory() throws Exception {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(DEFAULT_BROKER_URL);
        CamelContext context = camelContext();
        ActiveMQComponent comp = ActiveMQComponent.activeMQComponent(DEFAULT_BROKER_URL);
        comp.start();
        context.addComponent("jms",comp);
        context.addRoutes(new JmsToEmailRoute());
        context.start();
        connectionFactory.setTrustAllPackages(true);
        return connectionFactory;
    }
    /*
     * Optionally you can use cached connection factory if performance is a big concern.
     */

    @Bean
    public ConnectionFactory cachingConnectionFactory() throws Exception {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setTargetConnectionFactory(connectionFactory());
        connectionFactory.setSessionCacheSize(10);
        return connectionFactory;
    }

    /*
     * Used for Sending Messages.
     */
    @Bean
    public JmsTemplate jmsTemplate() throws Exception {
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
