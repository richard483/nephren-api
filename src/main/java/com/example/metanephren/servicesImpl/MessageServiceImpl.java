package com.example.metanephren.servicesImpl;

import com.example.metanephren.models.Message;
import com.example.metanephren.models.requests.MessageRequestVo;
import com.example.metanephren.helper.util.JWTUtil;
import com.example.metanephren.services.MessageService;
import com.example.metanephren.services.message.MessageProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

  private final MessageProducerService messageProducerService;
  private final JWTUtil jwtUtil;

  public MessageServiceImpl(MessageProducerService messageProducerService, JWTUtil jwtUtil) {
    this.messageProducerService = messageProducerService;
    this.jwtUtil = jwtUtil;
  }

  @Override
  public Mono<Void> sendMessage(MessageRequestVo messageRequest, String token) {
    Message message =
        Message.builder()
            .id(UUID.randomUUID().toString())
            .message(messageRequest.getMessage())
            .senderUsername(jwtUtil.getUsernameFromToken(token))
            .timestamp(
                Instant.now().getEpochSecond())
            .build();
    messageProducerService.send(message);
    return Mono.empty();
  }
}
