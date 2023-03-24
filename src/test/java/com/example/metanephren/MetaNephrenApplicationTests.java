package com.example.metanephren;

import com.example.metanephren.models.Role;
import com.example.metanephren.models.User;
import com.example.metanephren.repositories.ImageGridFsRepository;
import com.example.metanephren.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MetaNephrenApplicationTests {

  private static final String USER_PASSWORD = "password123@#PASS";
  private static final String USER_NAME = "password123@#PASS";
  ImageGridFsRepository imageGridFsRepository;
  UserRepository userRepository;

  @Autowired
  public MetaNephrenApplicationTests(ImageGridFsRepository imageGridFsRepository,
      UserRepository userRepository) {
    this.imageGridFsRepository = imageGridFsRepository;
    this.userRepository = userRepository;
  }

  @AfterEach
  void afterEach() {
    this.imageGridFsRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  void dummyTest() {
    userRepository.findUserByUsername(USER_NAME).subscribe(user -> Assertions.assertNull(user));

    userRepository.save(User.builder()
        .roles(List.of(Role.ROLE_MEMBER))
        .enabled(true)
        .password(USER_PASSWORD)
        .username(USER_NAME)
        .build());

    userRepository.findUserByUsername(USER_NAME).subscribe(user -> Assertions.assertNotNull(user));
  }

  @Test
  void dummyTest2() {
    userRepository.findUserByUsername(USER_NAME).subscribe(user -> Assertions.assertNull(user));

    userRepository.save(User.builder()
        .roles(List.of(Role.ROLE_MEMBER))
        .enabled(true)
        .password(USER_PASSWORD)
        .username(USER_NAME)
        .build());

    userRepository.findUserByUsername(USER_NAME).subscribe(user -> Assertions.assertNotNull(user));
  }

  @Test
  void dummyTest3() {
    userRepository.findUserByUsername(USER_NAME).subscribe(user -> Assertions.assertNull(user));

    userRepository.save(User.builder()
        .roles(List.of(Role.ROLE_MEMBER))
        .enabled(true)
        .password(USER_PASSWORD)
        .username(USER_NAME)
        .build());

    userRepository.findUserByUsername(USER_NAME).subscribe(user -> Assertions.assertNotNull(user));
  }

  @Test
  void dummyTest4() {
    userRepository.findUserByUsername(USER_NAME).subscribe(user -> Assertions.assertNull(user));

    userRepository.save(User.builder()
        .roles(List.of(Role.ROLE_MEMBER))
        .enabled(true)
        .password(USER_PASSWORD)
        .username(USER_NAME)
        .build());

    userRepository.findUserByUsername(USER_NAME).subscribe(user -> Assertions.assertNotNull(user));
  }
}
