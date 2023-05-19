package com.nephren.nephrenapi.services.message;

import com.nephren.nephrenapi.models.Message;
import reactor.core.publisher.Flux;

public interface MessageConsumerService {
  Flux<Message> messageConsumer();
}
