package com.nephren.nephrenapi.services.message;

import com.nephren.nephrenapi.models.Message;

public interface MessageProducerService {
  void send(Message message);
}
