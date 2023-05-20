package com.nephren.nephrenapi.servicesImpl;

import com.nephren.nephrenapi.models.responses.LoginResponse;
import com.nephren.nephrenapi.models.responses.LoginResponseVo;
import com.nephren.nephrenapi.models.responses.MetaNephrenBaseResponse;
import com.nephren.nephrenapi.models.responses.RegisterResponse;
import com.nephren.nephrenapi.models.responses.RegisterResponseVo;
import com.nephren.nephrenapi.services.ModelConverterService;
import org.springframework.stereotype.Service;

@Service
public class ModelConverterServiceImpl implements ModelConverterService {
  @Override
  public MetaNephrenBaseResponse<LoginResponse> createLoginResponse(LoginResponseVo responseVo) {
    return MetaNephrenBaseResponse.<LoginResponse>builder().body(LoginResponse.builder().token(
            responseVo.getToken()).build())
        .errorCode(responseVo.getErrorCode())
        .errorMessage(responseVo.getErrorMessage())
        .success(responseVo.isSuccess())
        .build();
  }

  @Override
  public MetaNephrenBaseResponse<RegisterResponse> createRegisterResponse(RegisterResponseVo responseVo) {
    return MetaNephrenBaseResponse.<RegisterResponse>builder()
        .body(RegisterResponse.builder()
            .role(responseVo.getRole()).username(responseVo.getUsername()).build())
        .errorCode(responseVo.getErrorCode())
        .errorMessage(responseVo.getErrorMessage())
        .success(responseVo.isSuccess())
        .build();
  }
}
