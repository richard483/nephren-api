package com.example.metanephren.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
  private String id;
  private String message;
  private String senderId;
  private String receiverId;
  private Date time;
}
