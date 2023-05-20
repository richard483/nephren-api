package com.nephren.nephrenapi.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseVo {
  private String token;
  private String errorCode;
  private String errorMessage;
  private boolean success;
}
