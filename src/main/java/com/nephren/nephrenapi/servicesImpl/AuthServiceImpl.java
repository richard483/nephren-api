package com.nephren.nephrenapi.servicesImpl;

import com.nephren.nephrenapi.helper.util.JWTUtil;
import com.nephren.nephrenapi.models.Role;
import com.nephren.nephrenapi.models.User;
import com.nephren.nephrenapi.models.requests.AuthRequestVo;
import com.nephren.nephrenapi.models.requests.RegisterRequestVo;
import com.nephren.nephrenapi.models.responses.LoginResponseVo;
import com.nephren.nephrenapi.models.responses.RegisterResponseVo;
import com.nephren.nephrenapi.repositories.UserRepository;
import com.nephren.nephrenapi.securities.PBKDF2Encoder;
import com.nephren.nephrenapi.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

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
  public Mono<LoginResponseVo> login(AuthRequestVo requestVo) {
    return userRepository.findUserByUsername(requestVo.getUsername())
        .filter(u -> pbkdf2Encoder.encode(requestVo.getPassword()).equals(u.getPassword()))
        .map(u -> LoginResponseVo.builder().token(jwtUtil.generateToken(u)).success(true).build())
        .switchIfEmpty(Mono.just(LoginResponseVo.builder()
            .success(false)
            .errorCode(HttpStatus.BAD_REQUEST.toString())
            .errorMessage("Wrong email or password")
            .build()));
  }

  @Override
  public Mono<RegisterResponseVo> register(RegisterRequestVo requestVo) {
    return userRepository.findUserByUsername(requestVo.getUsername())
        .map(user -> RegisterResponseVo.builder()
            .errorMessage("The username " + user.getUsername() + " has used!")
            .errorCode(HttpStatus.IM_USED.toString())
            .build())
        .switchIfEmpty(userRepository.save(User.builder()
                .username(requestVo.getUsername())
                .password(pbkdf2Encoder.encode(requestVo.getPassword()))
                .roles(List.of(Role.ROLE_MEMBER))
                .enabled(true)
                .build())
            .map(user1 -> RegisterResponseVo.builder()
                .username(user1.getUsername())
                .role(user1.getRoles())
                .success(true)
                .build()));
  }
}
