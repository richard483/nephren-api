package com.example.metanephren.services.kafka;

import com.example.metanephren.models.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class KafkaConsumerServices {
  private final ReactiveKafkaConsumerTemplate<String, Message> reactiveKafkaConsumerTemplate;

  public KafkaConsumerServices(ReactiveKafkaConsumerTemplate<String, Message> reactiveKafkaConsumerTemplate) {
    this.reactiveKafkaConsumerTemplate = reactiveKafkaConsumerTemplate;
  }

  public Flux<Message> messageConsumer() {
    return reactiveKafkaConsumerTemplate.receiveAutoAck()
        .doOnNext(record -> log.info(
            "#messageConsumer received record with key : {}, value : {}, topic : {}, offset, "
                + "{}",
            record.key(),
            record.value(),
            record.topic(),
            record.offset()))
        .map(ConsumerRecord::value)
        .doOnNext(message -> log.info("#messageConsumer success consuming {}",
            message.getClass().getSimpleName()))
        .doOnError(throwable -> log.error("#messageConsumer error while because : {}",
            throwable.getMessage()));
  }
}
