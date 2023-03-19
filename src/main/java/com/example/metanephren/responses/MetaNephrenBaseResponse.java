package com.example.metanephren.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MetaNephrenBaseResponse<T> {
  T body;
  private String errorMessage;
  private String errorCode;
  private Boolean success;
}
