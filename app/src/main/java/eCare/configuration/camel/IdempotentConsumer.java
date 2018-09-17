package eCare.configuration.camel;

import org.apache.camel.*;
import org.apache.camel.processor.idempotent.IdempotentOnCompletion;
import org.apache.camel.processor.idempotent.NoMessageIdException;
import org.apache.camel.spi.ExchangeIdempotentRepository;
import org.apache.camel.spi.IdAware;
import org.apache.camel.spi.IdempotentRepository;
import org.apache.camel.spi.Synchronization;
import org.apache.camel.support.ServiceSupport;
import org.apache.camel.util.AsyncProcessorConverterHelper;
import org.apache.camel.util.AsyncProcessorHelper;
import org.apache.camel.util.ServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by echerkas on 01.09.2018.
 */

public class IdempotentConsumer extends ServiceSupport implements CamelContextAware,
        AsyncProcessor, Navigate<Processor>, IdAware {

    private static final Logger LOG = LoggerFactory.getLogger(IdempotentConsumer.class);
    private CamelContext camelContext;
    private String id;
    private final Expression messageIdExpression;
    private final AsyncProcessor processor;
    private final IdempotentRepository<String> idempotentRepository;
    private final boolean eager;
    private final boolean completionEager;
    private final boolean skipDuplicate;
    private final boolean removeOnFailure;
    private final AtomicLong duplicateMessageCount = new AtomicLong();

    public  IdempotentConsumer(Expression messageIdExpression, IdempotentRepository<String> idempotentRepository,
                               boolean eager, boolean completionEager, boolean skipDuplicate, boolean removeOnFailure, Processor processor) {
        this.messageIdExpression = messageIdExpression;
        this.idempotentRepository = idempotentRepository;
        this.eager = eager;
        this.completionEager = completionEager;
        this.skipDuplicate = skipDuplicate;
        this.removeOnFailure = removeOnFailure;
        this.processor = AsyncProcessorConverterHelper.convert(processor);
    }

    @Override
    public   String toString() {
        return "IdempotentConsumer[" + messageIdExpression + " -> " + processor + "]";
    }

    @Override
    public  CamelContext getCamelContext() {
        return camelContext;
    }

    @Override
    public  void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    public  String getId() {
        return id;
    }

    public  void setId(String id) {
        this.id = id;
    }

    public void process(Exchange exchange) throws Exception {
        AsyncProcessorHelper.process(this, exchange);
    }

    public  boolean process(final Exchange exchange, final AsyncCallback callback) {
        final AsyncCallback target;

        final String messageId;
        try {
            messageId = messageIdExpression.evaluate(exchange, String.class);
            if (messageId == null) {
                exchange.setException(new NoMessageIdException(exchange, messageIdExpression));
                callback.done(true);
                return true;
            }
        } catch (Exception e) {
            exchange.setException(e);
            callback.done(true);
            return true;
        }

        try {
            boolean newKey;
            if (eager) {
                // add the key to the repository
                if (idempotentRepository instanceof ExchangeIdempotentRepository) {
                    newKey = ((ExchangeIdempotentRepository<String>) idempotentRepository).add(exchange, messageId);
                } else {
                    newKey = idempotentRepository.add(messageId);
                }
            } else {
                // check if we already have the key
                if (idempotentRepository instanceof ExchangeIdempotentRepository) {
                    newKey = !((ExchangeIdempotentRepository<String>) idempotentRepository).contains(exchange, messageId);
                } else {
                    newKey = !idempotentRepository.contains(messageId);
                }
            }

            if (!newKey) {
                // mark the exchange as duplicate
                exchange.setProperty(Exchange.DUPLICATE_MESSAGE, Boolean.TRUE);

                // we already have this key so its a duplicate message
                onDuplicate(exchange, messageId);

                if (skipDuplicate) {
                    // if we should skip duplicate then we are done
                    LOG.debug("Ignoring duplicate message with id: {} for exchange: {}", messageId, exchange);
                    callback.done(true);
                    return true;
                }
            }

            final Synchronization onCompletion = new IdempotentOnCompletion(idempotentRepository, messageId, eager, removeOnFailure);
            target = new IdempotentConsumerCallback(exchange, onCompletion, callback, completionEager);
            if (!completionEager) {
                // the scope is to do the idempotent completion work as an unit of work on the exchange when its done being routed
                exchange.addOnCompletion(onCompletion);
            }
        } catch (Exception e) {
            exchange.setException(e);
            callback.done(true);
            return true;
        }

        // process the exchange
        return processor.process(exchange, target);
    }

    public List<Processor> next() {
        if (!hasNext()) {
            return null;
        }
        List<Processor> answer = new ArrayList<Processor>(1);
        answer.add(processor);
        return answer;
    }

    public  boolean hasNext() {
        return processor != null;
    }

    // Properties
    // -------------------------------------------------------------------------
    public  Expression getMessageIdExpression() {
        return messageIdExpression;
    }

    public  IdempotentRepository<String> getIdempotentRepository() {
        return idempotentRepository;
    }

    public  Processor getProcessor() {
        return processor;
    }

    public  long getDuplicateMessageCount() {
        return duplicateMessageCount.get();
    }

    // Implementation methods
    // -------------------------------------------------------------------------

    protected  void doStart() throws Exception {
        // must add before start so it will have CamelContext injected first
        if (!camelContext.hasService(idempotentRepository)) {
            camelContext.addService(idempotentRepository);
        }
        ServiceHelper.startServices(processor, idempotentRepository);
    }

    protected  void doStop() throws Exception {
        ServiceHelper.stopServices(processor, idempotentRepository);
    }

    @Override
    protected  void doShutdown() throws Exception {
        ServiceHelper.stopAndShutdownServices(processor, idempotentRepository);
        camelContext.removeService(idempotentRepository);
    }

    public  boolean isEager() {
        return eager;
    }

    public  boolean isCompletionEager() {
        return completionEager;
    }

    public  boolean isSkipDuplicate() {
        return skipDuplicate;
    }

    public  boolean isRemoveOnFailure() {
        return removeOnFailure;
    }

    /**
     * Resets the duplicate message counter to <code>0L</code>.
     */
    public  void resetDuplicateMessageCount() {
        duplicateMessageCount.set(0L);
    }

    private  void onDuplicate(Exchange exchange, String messageId) {
        duplicateMessageCount.incrementAndGet();

        onDuplicateMessage(exchange, messageId);
    }

    /**
     * Clear the idempotent repository
     */
    public  void clear() {
        idempotentRepository.clear();
    }

    /**
     * A strategy method to allow derived classes to overload the behaviour of
     * processing a duplicate message
     *
     * @param exchange  the exchange
     * @param messageId the message ID of this exchange
     */
    protected  void onDuplicateMessage(Exchange exchange, String messageId) {
        // noop
    }

    /**
     * {@link org.apache.camel.AsyncCallback} that is invoked when the idempotent consumer block ends
     */
    private static class IdempotentConsumerCallback implements AsyncCallback {
        private final Exchange exchange;
        private final Synchronization onCompletion;
        private final AsyncCallback callback;
        private final boolean completionEager;

        IdempotentConsumerCallback(Exchange exchange, Synchronization onCompletion, AsyncCallback callback, boolean completionEager) {
            this.exchange = exchange;
            this.onCompletion = onCompletion;
            this.callback = callback;
            this.completionEager = completionEager;
        }

        @Override
        public  void done(boolean doneSync) {
            try {
                if (completionEager) {
                    if (exchange.isFailed()) {
                        onCompletion.onFailure(exchange);
                    } else {
                        onCompletion.onComplete(exchange);
                    }
                }
                // if completion is not eager then the onCompletion is invoked as part of the UoW of the Exchange
            } finally {
                callback.done(doneSync);
            }
        }

        @Override
        public String toString() {
            return "IdempotentConsumerCallback";
        }
    }
}
