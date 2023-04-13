package com.example.metanephren.services;

import com.example.metanephren.models.requests.MessageRequestVo;
import reactor.core.publisher.Mono;

public interface MessageService {
  Mono<Void> sendMessage(MessageRequestVo messageRequest, String token);
}
