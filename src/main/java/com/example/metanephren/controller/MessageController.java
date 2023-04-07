package com.example.metanephren.controller;

import com.example.metanephren.models.Message;
import com.example.metanephren.services.kafka.KafkaConsumerService;
import com.example.metanephren.services.kafka.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/messenger")
public class MessageController {
  public final KafkaConsumerService kafkaConsumerService;
  public final KafkaProducerService kafkaProducerService;

  @Autowired
  public MessageController(KafkaConsumerService kafkaConsumerService,
      KafkaProducerService kafkaProducerService) {
    this.kafkaConsumerService = kafkaConsumerService;
    this.kafkaProducerService = kafkaProducerService;
  }

  @PutMapping("/put")
  public void putKafka(@RequestBody Message message) {
    kafkaProducerService.send(message);
  }
}
