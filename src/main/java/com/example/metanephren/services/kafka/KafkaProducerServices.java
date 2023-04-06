package com.example.metanephren.services.kafka;

import com.example.metanephren.models.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerServices {
  private final ReactiveKafkaProducerTemplate<String, Message> reactiveKafkaProducerTemplate;

  @Value("${spring.kafka.template.default-topic}")
  private String topic;

  public KafkaProducerServices(ReactiveKafkaProducerTemplate<String, Message> reactiveKafkaProducerTemplate) {
    this.reactiveKafkaProducerTemplate = reactiveKafkaProducerTemplate;
  }

  public void send(Message message){
    reactiveKafkaProducerTemplate.send(topic, "this is key",  message).subscribe();
  }
}
