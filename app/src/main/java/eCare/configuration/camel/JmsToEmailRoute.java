package eCare.configuration.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.idempotent.MemoryIdempotentRepository;




/**
 * Created by echerkas on 02.09.2018.
 */
//@Component
public class JmsToEmailRoute extends RouteBuilder {

    @Override

    public void configure() throws Exception {
        errorHandler(deadLetterChannel("mock:error"));
        from("activemq:queue:TestQueue")
                .idempotentConsumer(header("JMSCorrelationID"),
                        MemoryIdempotentRepository.memoryIdempotentRepository(200))
                .removeOnFailure(true)
                .skipDuplicate(true)
                .to("activemq:queue:TestQueue")
                .to("stream:out");
    }
}
