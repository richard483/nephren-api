package com.nephren.nephrenapi.services;

import com.nephren.nephrenapi.models.requests.MessageRequestVo;
import reactor.core.publisher.Mono;

public interface MessageService {
  Mono<Void> sendMessage(MessageRequestVo messageRequest, String token);
}
