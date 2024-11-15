package enit.ecomerce.search_product.configuration;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.config.TopicBuilder;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    public KafkaAdmin admin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic topic1() {
        return TopicBuilder.name("product.created")
                .partitions(10)
                .compact()
                .build();
    } 
    
    @Bean
    public NewTopic topic2() {
        return TopicBuilder.name("product.deleted")
                .partitions(10)
                .compact()
                .build();
    } 

    @Bean
    public NewTopic topic3() {
        return TopicBuilder.name("product.modified")
                .partitions(10)
                .compact()
                .build();
    }

   
 
}
