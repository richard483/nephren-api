package com.example.metanephren;

import com.example.metanephren.services.kafka.KafkaConsumerService;
import com.example.metanephren.services.kafka.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableReactiveMongoRepositories
@SpringBootApplication
@Slf4j
public class MetaNephrenApplication implements CommandLineRunner {

  @Autowired
  KafkaProducerService kafkaProducerService;
  @Autowired
  KafkaConsumerService kafkaConsumerService;

  public static void main(String[] args) {
    SpringApplication.run(MetaNephrenApplication.class, args);
  }


  @Override
  public void run(String... args) {
    //    kafkaProducerServices.send(Message.builder().message("Halobang").build());
    //    kafkaProducerServices.send(Message.builder().message("Halobang1").build());
    //    kafkaProducerServices.send(Message.builder().message("Halobang2").build());
    //    kafkaConsumerServices.messageConsumer().subscribe(message -> log.info("#consuming message : "
    //        + "{}", message));
  }
}
