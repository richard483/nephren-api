package com.example.metanephren.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MetaNephrenBaseListResponse<T> {
  List<T> body = new ArrayList<>();
  private String errorMessage;
  private String errorCode;
  private Boolean success;
}
