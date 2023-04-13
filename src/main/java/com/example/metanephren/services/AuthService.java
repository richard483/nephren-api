package com.example.metanephren.services;

import com.example.metanephren.models.requests.AuthRequestVo;
import com.example.metanephren.models.requests.RegisterRequestVo;
import com.example.metanephren.models.responses.MetaNephrenBaseResponse;
import reactor.core.publisher.Mono;

public interface AuthService {
  Mono<MetaNephrenBaseResponse<Object>> login(AuthRequestVo requestVo);

  Mono<MetaNephrenBaseResponse<Object>> register(RegisterRequestVo requestVo);
}
