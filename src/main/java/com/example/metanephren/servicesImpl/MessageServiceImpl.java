package com.example.metanephren.servicesImpl;

import com.example.metanephren.models.Message;
import com.example.metanephren.models.requests.MessageRequestVo;
import com.example.metanephren.securities.JWTUtil;
import com.example.metanephren.services.MessageService;
import com.example.metanephren.services.kafka.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

  private final KafkaProducerService kafkaProducerService;
  private final JWTUtil jwtUtil;

  public MessageServiceImpl(KafkaProducerService kafkaProducerService, JWTUtil jwtUtil) {
    this.kafkaProducerService = kafkaProducerService;
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
    kafkaProducerService.send(message);
    return Mono.empty();
  }
}
