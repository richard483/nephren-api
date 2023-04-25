package com.example.metanephren.servicesImpl;

import com.example.metanephren.helper.util.JWTUtil;
import com.example.metanephren.models.User;
import com.example.metanephren.repositories.UserRepository;
import com.example.metanephren.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final JWTUtil jwtUtil;

  @Autowired
  public UserServiceImpl(UserRepository userRepository, JWTUtil jwtUtil) {
    this.userRepository = userRepository;
    this.jwtUtil = jwtUtil;
  }

  @Override
  public Mono<User> createUser(User user) {
    return userRepository.save(user);
  }

  @Override
  public Flux<User> getAllUser() {
    return userRepository.findAll();
  }

  @Override
  public Mono<User> getProfile(String token) {
    String username = jwtUtil.getUsernameFromToken(token);
    return userRepository.findUserByUsername(username);
  }
}
