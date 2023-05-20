package com.nephren.nephrenapi.services;

import com.nephren.nephrenapi.models.requests.AuthRequestVo;
import com.nephren.nephrenapi.models.requests.RegisterRequestVo;
import com.nephren.nephrenapi.models.responses.LoginResponseVo;
import com.nephren.nephrenapi.models.responses.RegisterResponseVo;
import reactor.core.publisher.Mono;

public interface AuthService {
  Mono<LoginResponseVo> login(AuthRequestVo requestVo);

  Mono<RegisterResponseVo> register(RegisterRequestVo requestVo);
}
