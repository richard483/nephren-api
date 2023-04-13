package com.example.metanephren.controller;

import com.example.metanephren.models.requests.MessageRequestVo;
import com.example.metanephren.services.MessageService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("api/messenger")
public class MessageController {
  public final MessageService messageService;

  public MessageController(MessageService messageService) {
    this.messageService = messageService;
  }

  @PutMapping("/put")
  public Mono<Void> putKafka(@RequestBody MessageRequestVo messageRequest,
      @RequestHeader("Authorization") String token) {
    return messageService.sendMessage(messageRequest, token);
  }
}
