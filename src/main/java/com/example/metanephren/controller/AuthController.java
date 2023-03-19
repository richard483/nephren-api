package com.example.metanephren.controller;

import com.example.metanephren.models.Role;
import com.example.metanephren.models.User;
import com.example.metanephren.repositories.UserRepository;
import com.example.metanephren.request.AuthRequestVo;
import com.example.metanephren.request.RegisterRequestVo;
import com.example.metanephren.responses.MetaNephrenBaseResponse;
import com.example.metanephren.securities.JWTUtil;
import com.example.metanephren.securities.PBKDF2Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class AuthController {
  private final UserRepository repository;
  private final JWTUtil jwtUtil;
  private final PBKDF2Encoder passwordEncoder;

  @Autowired
  public AuthController(UserRepository repository, JWTUtil jwtUtil, PBKDF2Encoder passwordEncoder) {
    this.repository = repository;
    this.jwtUtil = jwtUtil;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping("/login")
  public Mono<MetaNephrenBaseResponse<Object>> login(@RequestBody AuthRequestVo request) {
    return repository.findUserByUsername(request.getUsername())
        .filter(u -> passwordEncoder.encode(request.getPassword()).equals(u.getPassword()))
        .map(u -> MetaNephrenBaseResponse.builder()
            .body(jwtUtil.generateToken(u))
            .success(true)
            .build())
        .switchIfEmpty(Mono.just(MetaNephrenBaseResponse.builder()
            .success(false)
            .errorCode(HttpStatus.BAD_REQUEST.toString())
            .errorMessage("Wrong email or password")
            .build()));
  }

  @PostMapping("/register")
  public Mono<User> register(@RequestBody RegisterRequestVo request) {

    return repository.save(User.builder()
        .username(request.getUsername())
        .password(passwordEncoder.encode(request.getPassword()))
        .roles(List.of(Role.ROLE_MEMBER))
        .enabled(true)
        .build());
  }
}
