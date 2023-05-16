package com.example.metanephren.services.message;

import com.example.metanephren.models.Message;
import reactor.core.publisher.Flux;

public interface MessageConsumerService {
  Flux<Message> messageConsumer();
}
