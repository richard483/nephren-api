package com.example.metanephren.services.kafka;

import com.example.metanephren.models.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducerService {
  private final ReactiveKafkaProducerTemplate<String, Message> reactiveKafkaProducerTemplate;

  @Value("${spring.kafka.template.default-topic}")
  private String topic;

  public KafkaProducerService(ReactiveKafkaProducerTemplate<String, Message> reactiveKafkaProducerTemplate) {
    this.reactiveKafkaProducerTemplate = reactiveKafkaProducerTemplate;
  }

  public void send(Message message){
    reactiveKafkaProducerTemplate.send(topic,  message).subscribe();
    log.info("#KafkaProducerServices success send message : {}, to topic : {}",
        message, topic);
  }
}
