package com.nephren.nephrenapi.services;

import com.nephren.nephrenapi.models.requests.AuthRequestVo;
import com.nephren.nephrenapi.models.requests.RegisterRequestVo;
import com.nephren.nephrenapi.models.responses.MetaNephrenBaseResponse;
import reactor.core.publisher.Mono;

public interface AuthService {
  Mono<MetaNephrenBaseResponse<Object>> login(AuthRequestVo requestVo);

  Mono<MetaNephrenBaseResponse<Object>> register(RegisterRequestVo requestVo);
}
