package com.nephren.nephrenapi.repositories;

import com.nephren.nephrenapi.models.Message;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MessageRepository extends ReactiveMongoRepository<Message, String> {
}
