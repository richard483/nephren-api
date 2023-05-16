package com.example.metanephren.servicesImpl.mongo;

import com.example.metanephren.models.Message;
import com.example.metanephren.services.message.MessageConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.core.ChangeStreamEvent;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Objects;

@Service
@Slf4j
@ConditionalOnProperty(value = "kafka.enabled",
    havingValue = "false")
public class MongoConsumerServiceImpl implements MessageConsumerService {

  private final ReactiveMongoTemplate mongoTemplate;

  public MongoConsumerServiceImpl(ReactiveMongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public Flux<Message> messageConsumer() {
    return Objects.requireNonNull(mongoTemplate.changeStream(Message.class)
        .listen().map(ChangeStreamEvent::getBody)
        .doOnNext(System.out::println).log());
  }
}
