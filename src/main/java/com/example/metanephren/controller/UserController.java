package com.example.metanephren.controller;

import com.example.metanephren.models.User;
import com.example.metanephren.repositories.UserRepository;
import com.example.metanephren.responses.MetaNephrenBaseListResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("api/user")
public class UserController {

  private final UserRepository userRepository;

  @Autowired
  public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @PostMapping("/create")
  @PreAuthorize("hasRole('ADMIN')")
  public Mono<User> createUser(@RequestBody User user) {
    return userRepository.save(user);
  }

  @GetMapping("/getAll")
  @PreAuthorize("hasRole('ADMIN')")
  public Mono<MetaNephrenBaseListResponse<User>> getAll() {
    return userRepository.findAll()
        .collectList()
        .map(users -> MetaNephrenBaseListResponse.<User>builder().body(users).build());
  }
}
