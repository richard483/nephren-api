package com.nephren.nephrenapi.controller;

import com.nephren.nephrenapi.models.requests.AuthRequestVo;
import com.nephren.nephrenapi.models.requests.RegisterRequestVo;
import com.nephren.nephrenapi.models.responses.LoginResponse;
import com.nephren.nephrenapi.models.responses.MetaNephrenBaseResponse;
import com.nephren.nephrenapi.models.responses.RegisterResponse;
import com.nephren.nephrenapi.services.AuthService;
import com.nephren.nephrenapi.services.ModelConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("api")
public class AuthController {
  private final AuthService authService;
  private final ModelConverterService modelConverterService;
  @Value("${nephren-api-version}") private String appVersion;
  @Value("${nephren-application-name}") private String appName;
  @Value("${last-updated}") private String lastUpdated;


  @Autowired
  public AuthController(AuthService authService, ModelConverterService modelConverterService) {
    this.authService = authService;
    this.modelConverterService = modelConverterService;
  }

  @PostMapping("/login")
  public Mono<MetaNephrenBaseResponse<LoginResponse>> login(@RequestBody AuthRequestVo request) {
    return authService.login(request).map(modelConverterService::createLoginResponse);
  }

  @PostMapping("/register")
  public Mono<MetaNephrenBaseResponse<RegisterResponse>> register(@RequestBody RegisterRequestVo request) {

    return authService.register(request).map(modelConverterService::createRegisterResponse);
  }

  @GetMapping("/version")
  public Mono<MetaNephrenBaseResponse<Object>> version() {
    return Mono.just(MetaNephrenBaseResponse.builder()
        .body(Map.of("application",
            appName,
            "nephren-api version",
            appVersion,
            "last-updated",
            lastUpdated))
        .success(true)
        .build());
  }
}
