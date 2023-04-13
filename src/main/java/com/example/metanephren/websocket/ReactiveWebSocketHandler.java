package com.example.metanephren.websocket;

import com.example.metanephren.models.Message;
import com.example.metanephren.services.kafka.KafkaConsumerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ReactiveWebSocketHandler implements WebSocketHandler {
  private final Flux<Message> messageFlux;

  private final ObjectMapper objectMapper;

  @Autowired
  public ReactiveWebSocketHandler(KafkaConsumerService kafkaConsumerService,
      ObjectMapper objectMapper) {
    messageFlux = kafkaConsumerService.messageConsumer();
    this.objectMapper = objectMapper;
  }

  @Override
  public Mono<Void> handle(WebSocketSession session) {
    return session.send(messageFlux.handle((message, sink) -> {
      try {
        sink.next(session.textMessage(objectMapper.writeValueAsString(message)));
      } catch (JsonProcessingException e) {
        sink.error(new RuntimeException(e));
      }
    })).and(session.receive().map(WebSocketMessage::getPayloadAsText).log());
  }
}
