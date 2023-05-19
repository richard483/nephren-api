package com.nephren.nephrenapi.services;

import com.nephren.nephrenapi.models.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
  Mono<User> createUser(User user);

  Flux<User> getAllUser();

  Mono<User> getProfile(String token);
}
