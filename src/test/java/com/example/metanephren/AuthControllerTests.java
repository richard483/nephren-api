package com.example.metanephren;

import com.example.metanephren.models.Role;
import com.example.metanephren.models.User;
import com.example.metanephren.repositories.ImageGridFsRepository;
import com.example.metanephren.repositories.UserRepository;
import com.example.metanephren.models.requests.RegisterRequestVo;
import com.example.metanephren.securities.PBKDF2Encoder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;


@SpringBootTest()
@AutoConfigureWebTestClient
@Slf4j
class AuthControllerTests {

  private static final String USER_PASSWORD = "password123@#PASS";
  private static final String USER_NAME = "username";
  ImageGridFsRepository imageGridFsRepository;
  UserRepository userRepository;
  WebTestClient webTestClient;
  PBKDF2Encoder pbkdf2Encoder;

  @Autowired
  public AuthControllerTests(ImageGridFsRepository imageGridFsRepository,
      UserRepository userRepository,
      WebTestClient webTestClient,
      PBKDF2Encoder pbkdf2Encoder) {
    this.imageGridFsRepository = imageGridFsRepository;
    this.userRepository = userRepository;
    this.webTestClient = webTestClient;
    this.pbkdf2Encoder = pbkdf2Encoder;
  }

  @BeforeEach
  void beforeEach() {
    this.imageGridFsRepository.deleteAll().subscribe();
    this.userRepository.deleteAll().subscribe();
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
  void register_success() {
    RegisterRequestVo requestVo =
        RegisterRequestVo.builder().username(USER_NAME).password(USER_PASSWORD).build();

    webTestClient.post()
        .uri("http://localhost:8080/api/register")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .bodyValue(requestVo)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$['body'].['created'].['username']")
        .isEqualTo(USER_NAME)
        .jsonPath("$['body'].['created'].['password']")
        .isEqualTo(pbkdf2Encoder.encode(USER_PASSWORD));
  }

  @Test
  void register_fail_usernameAlreadyUsed() {
    userRepository.save(User.builder()
        .roles(List.of(Role.ROLE_MEMBER))
        .enabled(true)
        .password(USER_PASSWORD)
        .username(USER_NAME)
        .build()).subscribe();

    RegisterRequestVo requestVo =
        RegisterRequestVo.builder().username(USER_NAME).password(USER_PASSWORD).build();

    webTestClient.post()
        .uri("http://localhost:8080/api/register")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .bodyValue(requestVo)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$['body']")
        .isEqualTo(null)
        .jsonPath("$['errorMessage']")
        .isEqualTo("The username " + USER_NAME + " has used!")
        .jsonPath("$['errorCode']")
        .isEqualTo("400 BAD_REQUEST")
        .jsonPath("$['success']")
        .isEqualTo(false);
  }

  @Test
  void login_success() {
    userRepository.save(User.builder()
        .roles(List.of(Role.ROLE_MEMBER))
        .enabled(true)
        .password(pbkdf2Encoder.encode(USER_PASSWORD))
        .username(USER_NAME)
        .build()).subscribe();

    RegisterRequestVo requestVo =
        RegisterRequestVo.builder().username(USER_NAME).password(USER_PASSWORD).build();

    webTestClient.post()
        .uri("http://localhost:8080/api/login")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .bodyValue(requestVo)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$['body'].['token']")
        .isNotEmpty()
        .jsonPath("$['success']")
        .isEqualTo(true);
  }

  @Test
  void login_fail_wrongPassword() {
    userRepository.save(User.builder()
        .roles(List.of(Role.ROLE_MEMBER))
        .enabled(true)
        .password(pbkdf2Encoder.encode("DiffPassword"))
        .username(USER_NAME)
        .build()).subscribe();

    RegisterRequestVo requestVo =
        RegisterRequestVo.builder().username(USER_NAME).password(USER_PASSWORD).build();

    webTestClient.post()
        .uri("http://localhost:8080/api/login")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .bodyValue(requestVo)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$['errorMessage']")
        .isEqualTo("Wrong email or password")
        .jsonPath("$['errorCode']")
        .isEqualTo("400 BAD_REQUEST")
        .jsonPath("$['success']")
        .isEqualTo(false);
  }
}