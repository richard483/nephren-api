package com.nephren.nephrenapi.services;

import com.nephren.nephrenapi.models.responses.LoginResponse;
import com.nephren.nephrenapi.models.responses.LoginResponseVo;
import com.nephren.nephrenapi.models.responses.MetaNephrenBaseResponse;
import com.nephren.nephrenapi.models.responses.RegisterResponse;
import com.nephren.nephrenapi.models.responses.RegisterResponseVo;

public interface ModelConverterService {
  MetaNephrenBaseResponse<LoginResponse> createLoginResponse(LoginResponseVo responseVo);
  MetaNephrenBaseResponse<RegisterResponse> createRegisterResponse(RegisterResponseVo responseVo);
}
