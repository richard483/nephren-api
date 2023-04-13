package com.example.metanephren.servicesImpl.kafka;

import com.example.metanephren.models.Message;
import com.example.metanephren.services.kafka.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducerServiceImpl implements KafkaProducerService {
  private final ReactiveKafkaProducerTemplate<String, Message> reactiveKafkaProducerTemplate;

  @Value("${spring.kafka.template.default-topic}") private String topic;

  public KafkaProducerServiceImpl(ReactiveKafkaProducerTemplate<String, Message> reactiveKafkaProducerTemplate) {
    this.reactiveKafkaProducerTemplate = reactiveKafkaProducerTemplate;
  }

  public void send(Message message) {
    reactiveKafkaProducerTemplate.send(topic, message).subscribe();
    log.info("#KafkaProducerServices success send message : {}, to topic : {}", message, topic);
  }
}