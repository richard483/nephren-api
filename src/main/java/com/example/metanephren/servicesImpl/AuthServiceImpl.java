package com.example.metanephren.servicesImpl;

import com.example.metanephren.models.Role;
import com.example.metanephren.models.User;
import com.example.metanephren.models.requests.AuthRequestVo;
import com.example.metanephren.models.requests.RegisterRequestVo;
import com.example.metanephren.models.responses.MetaNephrenBaseResponse;
import com.example.metanephren.repositories.UserRepository;
import com.example.metanephren.securities.JWTUtil;
import com.example.metanephren.securities.PBKDF2Encoder;
import com.example.metanephren.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {
  private final UserRepository userRepository;
  private final JWTUtil jwtUtil;
  private final PBKDF2Encoder pbkdf2Encoder;

  @Autowired
  public AuthServiceImpl(UserRepository userRepository,
      JWTUtil jwtUtil,
      PBKDF2Encoder pbkdf2Encoder) {
    this.userRepository = userRepository;
    this.jwtUtil = jwtUtil;
    this.pbkdf2Encoder = pbkdf2Encoder;
  }

  @Override
  public Mono<MetaNephrenBaseResponse<Object>> login(AuthRequestVo requestVo) {
    return userRepository.findUserByUsername(requestVo.getUsername())
        .filter(u -> pbkdf2Encoder.encode(requestVo.getPassword()).equals(u.getPassword()))
        .map(u -> MetaNephrenBaseResponse.builder()
            .body(Map.of("token", jwtUtil.generateToken(u)))
            .success(true)
            .build())
        .switchIfEmpty(Mono.just(MetaNephrenBaseResponse.builder()
            .success(false)
            .errorCode(HttpStatus.BAD_REQUEST.toString())
            .errorMessage("Wrong email or password")
            .build()));
  }

  @Override
  public Mono<MetaNephrenBaseResponse<Object>> register(RegisterRequestVo requestVo) {
    return userRepository.findUserByUsername(requestVo.getUsername())
        .map(user -> MetaNephrenBaseResponse.builder()
            .errorMessage("The username " + user.getUsername() + " has used!")
            .errorCode(HttpStatus.BAD_REQUEST.toString())
            .success(false)
            .build())
        .switchIfEmpty(userRepository.save(User.builder()
                .username(requestVo.getUsername())
                .password(pbkdf2Encoder.encode(requestVo.getPassword()))
                .roles(List.of(Role.ROLE_MEMBER))
                .enabled(true)
                .build())
            .map(user1 -> MetaNephrenBaseResponse.builder()
                .body(Map.of("created", user1))
                .success(true)
                .build()));
  }
}