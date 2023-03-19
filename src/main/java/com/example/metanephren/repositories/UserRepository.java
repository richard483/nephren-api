package com.example.metanephren.repositories;

import com.example.metanephren.models.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {
  public Mono<User> findUserByUsername(String username);
}
