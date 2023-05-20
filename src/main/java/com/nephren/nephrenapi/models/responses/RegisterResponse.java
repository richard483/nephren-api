package com.nephren.nephrenapi.models.responses;

import com.nephren.nephrenapi.models.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
  private String username;
  private List<Role> role;
}
