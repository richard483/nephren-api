package com.example.metanephren.configurations.kafka;

import com.example.metanephren.models.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.Collections;

@Configuration
public class KafkaConsumerConfiguration {
  @Value("${spring.kafka.template.default-topic}")
  private String topic;

  @Bean
  public ReceiverOptions<String, Message> kafkaReceiverOptions(KafkaProperties kafkaProperties) {
    ReceiverOptions<String, Message> basicReceiverOptions =
        ReceiverOptions.create(kafkaProperties.buildConsumerProperties());
    return basicReceiverOptions.subscription(Collections.singleton(topic));
  }

  @Bean
  public ReactiveKafkaConsumerTemplate<String, Message> reactiveKafkaConsumerTemplate(
      ReceiverOptions<String, Message> kafkaReceiverOptions) {
    return new ReactiveKafkaConsumerTemplate<>(kafkaReceiverOptions);
  }
}
