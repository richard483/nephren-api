package com.nephren.nephrenapi;

import com.nephren.nephrenapi.helper.util.JWTUtil;
import com.nephren.nephrenapi.models.Image;
import com.nephren.nephrenapi.models.Role;
import com.nephren.nephrenapi.models.User;
import com.nephren.nephrenapi.models.responses.MetaNephrenBaseResponse;
import com.nephren.nephrenapi.repositories.ImageGridFsRepository;
import com.nephren.nephrenapi.repositories.UserRepository;
import com.nephren.nephrenapi.securities.PBKDF2Encoder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
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

  @AfterEach
  void afterEach() {
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
        .isNotEmpty()
        .jsonPath("$.body.name")
        .isEqualTo("rosemiShamaa")
        .jsonPath("$.body.desc")
        .isEqualTo("this is image of rosemi sama")
        .jsonPath("$.body.uploader")
        .isEqualTo(USER_NAME);
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

    byte[] imageResponse = webTestClient.post()
        .uri(LOCALHOST + "/api/photo/add?title=rosemiShamaa&desc=this is image of rosemi sama")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .header("Authorization", token)
        .body(BodyInserters.fromMultipartData("imageFile", fileMock.getResource()))
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .returnResult()
        .getResponseBody();

    MetaNephrenBaseResponse<Image> image =
        objectMapper.readValue(imageResponse, new TypeReference<>() {
        });

    webTestClient.get()
        .uri(LOCALHOST + "/api/photo/" + image.getBody().getId())
        .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .header("Authorization", token)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .equals(fileMock.getResource().getInputStream().readAllBytes());
  }

  @Test
  void getImage_noImageWithSuchId_fail() {
    User user = User.builder()
        .roles(List.of(Role.ROLE_MEMBER))
        .enabled(true)
        .password(pbkdf2Encoder.encode(USER_PASSWORD))
        .username(USER_NAME)
        .build();
    userRepository.save(user).subscribe();

    String token = jwtUtil.generateToken(user);

    webTestClient.get()
        .uri(LOCALHOST + "/api/photo/" + "5f9f1b5b0b1b9c2c8c8c8c8c")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .header("Authorization", token)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .isEmpty();
  }

  @Test
  void getPhotoInfo_success() throws IOException {
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

    MetaNephrenBaseResponse<Image> image =
        objectMapper.readValue(responseBody, new TypeReference<>() {
        });

    String id = image.getBody().getId();

    webTestClient.get()
        .uri(LOCALHOST + "/api/photo/info/" + id)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .header("Authorization", token)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.success")
        .isEqualTo(true)
        .jsonPath("$.body.id")
        .isEqualTo(id)
        .jsonPath("$.body.name")
        .isEqualTo("rosemiShamaa")
        .jsonPath("$.body.desc")
        .isEqualTo("this is image of rosemi sama")
        .jsonPath("$.body.uploader")
        .isEqualTo(USER_NAME);
  }

  @Test
  void getPhotoInfo_noImageWithSuchId_fail() {

    User user = User.builder()
        .roles(List.of(Role.ROLE_MEMBER))
        .enabled(true)
        .password(pbkdf2Encoder.encode(USER_PASSWORD))
        .username(USER_NAME)
        .build();
    userRepository.save(user).subscribe();

    String token = jwtUtil.generateToken(user);

    webTestClient.get()
        .uri(LOCALHOST + "/api/photo/info/" + "5f9f1b5b0b1b9c2c8c8c8c8c")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .header("Authorization", token)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .isEmpty();
  }

  @Test
  void getImageList_success() {
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
        .isNotEmpty()
        .returnResult()
        .getResponseBody();

    webTestClient.get()
        .uri(LOCALHOST + "/api/photo/info/page?page=0&contentPerPage=2")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .header("Authorization", token)
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
  void getImageList_emptyPage_success() {

    webTestClient.get()
        .uri(LOCALHOST + "/api/photo/info/page?page=0&contentPerPage=2")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.success")
        .isEqualTo(true)
        .jsonPath("$.body")
        .isEmpty();
  }
}
