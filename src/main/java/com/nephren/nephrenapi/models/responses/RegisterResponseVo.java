package com.nephren.nephrenapi.models.responses;

import com.nephren.nephrenapi.models.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponseVo {
  private String username;
  private List<Role> role;
  private String errorCode;
  private String errorMessage;
  private boolean success;
}
