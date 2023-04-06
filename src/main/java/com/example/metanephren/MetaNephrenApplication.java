package com.example.metanephren;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableReactiveMongoRepositories
@SpringBootApplication
@Slf4j
public class MetaNephrenApplication {

  public static void main(String[] args) {
    SpringApplication.run(MetaNephrenApplication.class, args);
  }
}
