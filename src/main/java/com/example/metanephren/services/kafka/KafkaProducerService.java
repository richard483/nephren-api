package com.example.metanephren.services.kafka;

import com.example.metanephren.models.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducerService {
  private final KafkaTemplate<String, Message> kafkaTemplate;

  @Value("${spring.kafka.template.default-topic}") private String topic;

  @Autowired
  public KafkaProducerService(KafkaTemplate<String, Message> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void send(Message message) {
    kafkaTemplate.send(topic, message).whenComplete((messageSendResult, throwable) -> {
      if (throwable != null) {
        log.error("#KafkaProducerServices error on send : {} to : {} caused by {}",
            message,
            topic,
            throwable.getMessage());
      } else {
        log.debug("#KafkaProducerServices success send : {} to : {}", message, topic);
      }
    });
  }
}
