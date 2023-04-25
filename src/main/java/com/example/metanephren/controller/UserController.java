package com.example.metanephren.controller;

import com.example.metanephren.models.User;
import com.example.metanephren.models.responses.MetaNephrenBaseListResponse;
import com.example.metanephren.models.responses.MetaNephrenBaseResponse;
import com.example.metanephren.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("api/user")
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/create")
  @PreAuthorize("hasRole('ADMIN')")
  public Mono<MetaNephrenBaseResponse<User>> createUser(@RequestBody User user) {
    return userService.createUser(user)
        .map(user1 -> MetaNephrenBaseResponse.<User>builder().success(true).body(user1).build());
  }

  @GetMapping("/getAll")
  @PreAuthorize("hasRole('ADMIN')")
  public Mono<MetaNephrenBaseListResponse<User>> getAll() {
    return userService.getAllUser()
        .collectList()
        .map(users -> MetaNephrenBaseListResponse.<User>builder().body(users).build());
  }

  @GetMapping("/profile")
  @PreAuthorize("hasRole('MEMBER')")
  public Mono<MetaNephrenBaseResponse<User>> getProfile(@RequestHeader("Authorization") String token) {
    return userService.getProfile(token)
        .map(user -> MetaNephrenBaseResponse.<User>builder().body(user).success(true).build());
  }
}
