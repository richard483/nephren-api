package com.example.metanephren.servicesImpl.kafka;

import com.example.metanephren.models.Message;
import com.example.metanephren.services.message.MessageConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
@ConditionalOnProperty(value = "kafka.enabled",
    havingValue = "true")
public class KafkaConsumerServiceImpl implements MessageConsumerService {
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
