package com.example.metanephren.controller;

import com.example.metanephren.models.Message;
import com.example.metanephren.services.kafka.KafkaConsumerServices;
import com.example.metanephren.services.kafka.KafkaProducerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/messenger")
public class MessageController {
  public final KafkaConsumerServices kafkaConsumerServices;
  public final KafkaProducerServices kafkaProducerServices;

  @Autowired
  public MessageController(KafkaConsumerServices kafkaConsumerServices, KafkaProducerServices kafkaProducerServices) {
    this.kafkaConsumerServices = kafkaConsumerServices;
    this.kafkaProducerServices = kafkaProducerServices;
  }

  @PutMapping("/put")
  public void putKafka(@RequestBody Message message){
    kafkaProducerServices.send(message);
  }
}
