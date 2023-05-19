package com.nephren.nephrenapi.servicesImpl.kafka;

import com.nephren.nephrenapi.models.Message;
import com.nephren.nephrenapi.services.message.MessageProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@ConditionalOnProperty(value = "kafka.enabled",
    havingValue = "true")
public class KafkaProducerServiceImpl implements MessageProducerService {
  private final ReactiveKafkaProducerTemplate<String, Message> reactiveKafkaProducerTemplate;

  @Value("${spring.kafka.template.default-topic}") private String topic;

  public KafkaProducerServiceImpl(ReactiveKafkaProducerTemplate<String, Message> reactiveKafkaProducerTemplate) {
    this.reactiveKafkaProducerTemplate = reactiveKafkaProducerTemplate;
  }

  public void send(Message message) {
    reactiveKafkaProducerTemplate.send(topic, message).subscribe();
    log.debug("#KafkaProducerServices success send message : {}, to topic : {}", message, topic);
  }
}
