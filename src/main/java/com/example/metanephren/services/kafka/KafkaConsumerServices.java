package com.example.metanephren.services.kafka;

import com.example.metanephren.models.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class KafkaConsumerServices {
  private final KafkaTemplate<String, Message> kafkaTemplate;
  @Value("${spring.kafka.template.default-topic}") private String topic;

  @Autowired
  public KafkaConsumerServices(KafkaTemplate<String, Message> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @KafkaListener(topics = "${spring.kafka.template.default-topic}",
      groupId = "${spring.kafka.consumer.group-id}")
  public void messageConsumer(@Payload Message message, @Headers MessageHeaders headers) {
    log.info("#KafkaConsumerServices consuming: {}", message);
  }
}
