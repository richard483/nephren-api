package com.example.metanephren;

import com.example.metanephren.helper.util.JWTUtil;
import com.example.metanephren.models.Image;
import com.example.metanephren.models.Role;
import com.example.metanephren.models.User;
import com.example.metanephren.repositories.ImageGridFsRepository;
import com.example.metanephren.repositories.UserRepository;
import com.example.metanephren.securities.PBKDF2Encoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.io.IOException;
import java.util.List;

@SpringBootTest()
@AutoConfigureWebTestClient
@Slf4j
public class ImageControllerIntegrationTest {
  private static final String LOCALHOST = "http://localhost:8080";
  private static final String USER_NAME = "USER_NAME";
  private static final String USER_PASSWORD = "PASSWORD123";
  WebTestClient webTestClient;
  ImageGridFsRepository imageGridFsRepository;
  UserRepository userRepository;
  PBKDF2Encoder pbkdf2Encoder;
  JWTUtil jwtUtil;
  ObjectMapper objectMapper;

  @Autowired
  public ImageControllerIntegrationTest(WebTestClient webTestClient,
      ImageGridFsRepository imageGridFsRepository,
      UserRepository userRepository,
      PBKDF2Encoder pbkdf2Encoder,
      JWTUtil jwtUtil,
      ObjectMapper objectMapper) {
    this.webTestClient = webTestClient;
    this.imageGridFsRepository = imageGridFsRepository;
    this.userRepository = userRepository;
    this.pbkdf2Encoder = pbkdf2Encoder;
    this.jwtUtil = jwtUtil;
    this.objectMapper = objectMapper;
  }

  @BeforeEach
  void beforeEach() {
    this.userRepository.deleteAll().subscribe();
    this.imageGridFsRepository.deleteAll().subscribe();
  }

  @Test
  void uploadImage_success() {
    MockMultipartFile fileMock = new MockMultipartFile("rosemiShamaa",
        "rosemiShamaa.jpg",
        MediaType.MULTIPART_FORM_DATA_VALUE,
        "payload".getBytes());

    User user = User.builder()
        .roles(List.of(Role.ROLE_MEMBER))
        .enabled(true)
        .password(pbkdf2Encoder.encode(USER_PASSWORD))
        .username(USER_NAME)
        .build();
    userRepository.save(user).subscribe();

    String token = jwtUtil.generateToken(user);

    webTestClient.post()
        .uri(LOCALHOST + "/api/photo/add?title=rosemiShamaa&desc=this is image of rosemi sama")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .header("Authorization", token)
        .body(BodyInserters.fromMultipartData("imageFile", fileMock.getResource()))
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.success")
        .isEqualTo(true)
        .jsonPath("$.body")
        .isNotEmpty();
  }

  @Test
  void uploadImage_noImage_fail() {
    User user = User.builder()
        .roles(List.of(Role.ROLE_MEMBER))
        .enabled(true)
        .password(pbkdf2Encoder.encode(USER_PASSWORD))
        .username(USER_NAME)
        .build();
    userRepository.save(user).subscribe();

    String token = jwtUtil.generateToken(user);

    webTestClient.post()
        .uri(LOCALHOST + "/api/photo/add?title=rosemiShamaa&desc=this is image of rosemi sama")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .header("Authorization", token)
        .exchange()
        .expectStatus()
        .isBadRequest();
  }

  @Test
  void getImage_success() throws IOException {
    MockMultipartFile fileMock = new MockMultipartFile("rosemiShamaa",
        "rosemiShamaa.jpg",
        MediaType.MULTIPART_FORM_DATA_VALUE,
        "payload".getBytes());

    User user = User.builder()
        .roles(List.of(Role.ROLE_MEMBER))
        .enabled(true)
        .password(pbkdf2Encoder.encode(USER_PASSWORD))
        .username(USER_NAME)
        .build();
    userRepository.save(user).subscribe();

    String token = jwtUtil.generateToken(user);

    webTestClient.post()
        .uri(LOCALHOST + "/api/photo/add?title=rosemiShamaa&desc=this is image of rosemi sama")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .header("Authorization", token)
        .body(BodyInserters.fromMultipartData("imageFile", fileMock.getResource()))
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.success")
        .isEqualTo(true)
        .jsonPath("$.body")
        .isNotEmpty();

    byte[] responseBody = webTestClient.post()
        .uri(LOCALHOST + "/api/photo/add?title=rosemiShamaa&desc=this is image of rosemi sama")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .header("Authorization", token)
        .body(BodyInserters.fromMultipartData("imageFile", fileMock.getResource()))
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.success")
        .isEqualTo(true)
        .jsonPath("$.body")
        .isNotEmpty()
        .returnResult()
        .getResponseBody();

    Image image = objectMapper.readValue(responseBody, Image.class);

    String id = image.getId();

    webTestClient.get()
        .uri(LOCALHOST + "/api/photo/" + id)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .header("Authorization", token)
        .exchange()
        .expectStatus()
        .is5xxServerError();
  }
}
