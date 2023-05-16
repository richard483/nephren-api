package com.example.metanephren.services.message;

import com.example.metanephren.models.Message;

public interface MessageProducerService {
  void send(Message message);
}
