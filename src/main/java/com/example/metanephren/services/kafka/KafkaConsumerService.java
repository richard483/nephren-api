package com.example.metanephren.services.kafka;

import com.example.metanephren.models.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class KafkaConsumerService {
  private final ReactiveKafkaConsumerTemplate<String, Message> reactiveKafkaConsumerTemplate;

  public KafkaConsumerService(ReactiveKafkaConsumerTemplate<String, Message> reactiveKafkaConsumerTemplate) {
    this.reactiveKafkaConsumerTemplate = reactiveKafkaConsumerTemplate;
  }

  public Flux<Message> messageConsumer() {
    return reactiveKafkaConsumerTemplate.receiveAutoAck()
        .map(ConsumerRecord::value)
        .doOnNext(message -> log.info("#KafkaConsumerServices messageConsumer success consuming {}",
            message.getClass().getSimpleName()))
        .doOnError(throwable -> log.error(
            "#KafkaConsumerServices messageConsumer error while " + "because : {}",
            throwable.getMessage()));
  }
}
