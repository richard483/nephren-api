package com.example.metanephren.services.kafka;

import com.example.metanephren.models.Message;
import reactor.core.publisher.Flux;

public interface KafkaConsumerService {
  Flux<Message> messageConsumer();
}
