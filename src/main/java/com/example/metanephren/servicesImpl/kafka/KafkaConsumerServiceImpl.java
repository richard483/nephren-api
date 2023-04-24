package com.example.metanephren.servicesImpl.kafka;

import com.example.metanephren.models.Message;
import com.example.metanephren.services.kafka.KafkaConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class KafkaConsumerServiceImpl implements KafkaConsumerService {
  private final ReactiveKafkaConsumerTemplate<String, Message> reactiveKafkaConsumerTemplate;

  public KafkaConsumerServiceImpl(ReactiveKafkaConsumerTemplate<String, Message> reactiveKafkaConsumerTemplate) {
    this.reactiveKafkaConsumerTemplate = reactiveKafkaConsumerTemplate;
  }

  @Override
  public Flux<Message> messageConsumer() {
    return reactiveKafkaConsumerTemplate.receiveAutoAck()
        .map(ConsumerRecord::value)
        .doOnNext(message -> log.debug("#KafkaConsumerServices messageConsumer success consuming {}",
            message.getClass().getSimpleName()))
        .doOnError(throwable -> log.error(
            "#KafkaConsumerServices messageConsumer error because : {}",
            throwable.getMessage()));
  }
}
