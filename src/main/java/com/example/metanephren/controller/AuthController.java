package com.example.metanephren.controller;

import com.example.metanephren.requests.AuthRequestVo;
import com.example.metanephren.requests.RegisterRequestVo;
import com.example.metanephren.responses.MetaNephrenBaseResponse;
import com.example.metanephren.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api")
public class AuthController {
  private final AuthService authService;

  @Autowired
  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/login")
  public Mono<MetaNephrenBaseResponse<Object>> login(@RequestBody AuthRequestVo request) {
    return authService.login(request);
  }

  @PostMapping("/register")
  public Mono<MetaNephrenBaseResponse<Object>> register(@RequestBody RegisterRequestVo request) {

    return authService.register(request);
  }
}
