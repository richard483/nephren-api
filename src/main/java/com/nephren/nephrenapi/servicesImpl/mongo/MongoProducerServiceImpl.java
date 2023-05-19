package com.nephren.nephrenapi.servicesImpl.mongo;

import com.nephren.nephrenapi.models.Message;
import com.nephren.nephrenapi.repositories.MessageRepository;
import com.nephren.nephrenapi.services.message.MessageProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@ConditionalOnProperty(value = "kafka.enabled",
    havingValue = "false")
public class MongoProducerServiceImpl implements MessageProducerService {

  private final MessageRepository messageRepository;

  public MongoProducerServiceImpl(MessageRepository messageRepository) {
    this.messageRepository = messageRepository;
  }

  @Override
  public void send(Message message) {
    messageRepository.save(message).subscribe();
    log.debug("#MongoProducerServices success send message : {}", message);
  }
}
