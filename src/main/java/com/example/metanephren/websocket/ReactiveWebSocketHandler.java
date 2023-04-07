package com.example.metanephren.websocket;

import com.example.metanephren.models.Message;
import com.example.metanephren.services.kafka.KafkaConsumerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class ReactiveWebSocketHandler implements WebSocketHandler {
  private final Flux<Message> intervalFlux;

  @Autowired
  public ReactiveWebSocketHandler(KafkaConsumerServices kafkaConsumerServices) {
    intervalFlux = Flux.interval(Duration.ofMillis(100L))
        .zipWith(kafkaConsumerServices.messageConsumer(), (aLong, message) -> message);
  }

  @Override
  public Mono<Void> handle(WebSocketSession session) {
    return session.send(intervalFlux.map(message -> session.textMessage(String.valueOf(message))))
        .and(session.receive().map(WebSocketMessage::getPayloadAsText).log());
  }
}
