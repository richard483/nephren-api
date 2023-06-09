package com.nephren.nephrenapi.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("messages")
public class Message {
  @Id private String id;
  private String message;
  private String senderUsername;
  private long timestamp;
}
