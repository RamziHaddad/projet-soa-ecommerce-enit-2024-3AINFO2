    package enit.ecomerce.search_product.configuration;

    import org.apache.kafka.clients.consumer.Consumer;
    import org.apache.kafka.clients.consumer.ConsumerRecord;
    import org.apache.kafka.common.errors.RecordDeserializationException;
    import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.listener.CommonErrorHandler;
    import org.springframework.kafka.listener.MessageListenerContainer;
 
    import org.springframework.stereotype.Component;

    import enit.ecomerce.search_product.emitter.EventsEmitter;
    import jakarta.annotation.PostConstruct;
 

    
    @Component
   public  class KafkaErrorHandler implements CommonErrorHandler {
    private final EventsEmitter eventsEmitter;
   
     public KafkaErrorHandler(@Lazy EventsEmitter eventsEmitter) {
        this.eventsEmitter = eventsEmitter;
    }

    @PostConstruct
    public void init() {
        if (eventsEmitter == null) {
            System.out.println("EventsEmitter is not injected");
        } else {
            System.out.println("EventsEmitter successfully injected");
        }
    }
        @Override
        public boolean handleOne(Exception exception, ConsumerRecord<?, ?> record, Consumer<?, ?> consumer, MessageListenerContainer container) {
            handle(exception, consumer, record);
            return true;
        }

        @Override
        public void handleOtherException(Exception exception, Consumer<?, ?> consumer, MessageListenerContainer container, boolean batchListener) {
            handle(exception, consumer, null);
        }

        private void handle(Exception exception, Consumer<?, ?> consumer, ConsumerRecord<?, ?> record) {  
            
    
                try { 
                    if (record != null) {
        

                        
                        String topic = record.topic();
                        int partition = record.partition();
                        long offset = record.offset();
                        Object key = record.key();
                        byte[] rawValue = (byte[]) record.value();
                        if (this.eventsEmitter == null) {
                            System.out.println("EventsEmitter is not injected");
                        }else {
                            System.out.println("EventsEmitter successfully injected");
                        }
                        this.eventsEmitter.emitToDeadLetterTopic(
                            topic,
                            partition,
                            offset,
                            key,
                            rawValue,
                            exception.getMessage(),
                            exception.getClass().getName()
                        );


        
                        consumer.seek(new org.apache.kafka.common.TopicPartition(topic, partition), offset + 1);

                        System.out.println("Record sent to dead-letter topic. Moving to next offset: " + (offset + 1));
                    }
                } catch (Exception e) {
                    System.out.println("Failed to handle deserialization exception and send record to dead-letter topic"+ e.getMessage());
                }
            }
        }
    


