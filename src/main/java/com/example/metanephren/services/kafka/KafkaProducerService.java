package com.example.metanephren.services.kafka;

import com.example.metanephren.models.Message;

public interface KafkaProducerService {
  void send(Message message);
}
