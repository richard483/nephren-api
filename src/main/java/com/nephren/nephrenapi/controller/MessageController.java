package com.nephren.nephrenapi.controller;

import com.nephren.nephrenapi.models.requests.MessageRequestVo;
import com.nephren.nephrenapi.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/messenger")
public class MessageController {
  private final MessageService messageService;

  @Autowired
  public MessageController(MessageService messageService) {
    this.messageService = messageService;
  }

  @PutMapping("/put")
  public Mono<Void> putKafka(@RequestBody MessageRequestVo messageRequest,
      @RequestHeader("Authorization") String token) {
    return messageService.sendMessage(messageRequest, token);
  }
}
