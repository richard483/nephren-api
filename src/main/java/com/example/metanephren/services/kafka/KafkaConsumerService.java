package com.example.metanephren.services.kafka;

import com.example.metanephren.models.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerService {

  @KafkaListener(topicPartitions = {
      @TopicPartition(topic = "${spring.kafka.template.default-topic}",
          partitionOffsets = @PartitionOffset(partition = "0",
              initialOffset = "0"))},
      groupId = "${spring.kafka.consumer.group-id}")
  public void messageConsumerFromBeginning(@Payload Message message,
      @Headers MessageHeaders headers) {
    log.info("#KafkaConsumerServices consuming: {}", message);
  }
}
