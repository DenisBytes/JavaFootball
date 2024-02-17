package com.github.denisbytes.aggregator.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaDataConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaDataConsumer.class);

    private static final String  BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String  GROUP_ID = "java-football-consumer-group";
    private static final String  TOPIC_NAME = "MatchHighlightsData";

    @KafkaListener(topics = TOPIC_NAME, groupId = GROUP_ID)
    public void listen(ConsumerRecord<String, String> record) {
        logger.info("Received message: {}", record.value());
    }
}
