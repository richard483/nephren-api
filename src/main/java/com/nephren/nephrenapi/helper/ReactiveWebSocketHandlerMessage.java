package com.nephren.nephrenapi.helper;

import com.nephren.nephrenapi.models.Message;
import com.nephren.nephrenapi.models.Role;
import com.nephren.nephrenapi.helper.util.JWTUtil;
import com.nephren.nephrenapi.services.message.MessageConsumerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class ReactiveWebSocketHandlerMessage implements WebSocketHandler {
  private final Flux<Message> messageFlux;

  private final ObjectMapper objectMapper;

  private final JWTUtil jwtUtil;

  @Autowired
  public ReactiveWebSocketHandlerMessage(MessageConsumerService messageConsumerService,
      ObjectMapper objectMapper,
      JWTUtil jwtUtil) {
    messageFlux = messageConsumerService.messageConsumer();
    this.objectMapper = objectMapper;
    this.jwtUtil = jwtUtil;
  }

  @Override
  public @NonNull Mono<Void> handle(WebSocketSession session) {
    Claims claims = jwtUtil.getAllClaimsFromToken(Objects.requireNonNull(session.getHandshakeInfo()
        .getHeaders()
        .get("Authorization")).get(0));

    return session.send(messageFlux.handle((message, sink) -> {
      try {
        if (!claims.get("role", List.class).contains(Role.ROLE_MEMBER.toString())) {
          sink.error(new Exception("Unauthorized, Login First"));
          return;
        }
        sink.next(session.textMessage(objectMapper.writeValueAsString(message)));
      } catch (JsonProcessingException e) {
        sink.error(new RuntimeException(e));
      }
    })).and(session.receive().map(WebSocketMessage::getPayloadAsText).log());
  }
}
