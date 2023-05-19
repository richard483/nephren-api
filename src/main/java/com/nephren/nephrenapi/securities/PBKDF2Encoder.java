package com.nephren.nephrenapi.securities;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Component
public class PBKDF2Encoder implements PasswordEncoder {

  @Value("${springbootwebfluxjjwt.password.encoder.secret}") private String secret;

  @Value("${springbootwebfluxjjwt.password.encoder.iteration}") private Integer iteration;

  @Value("${springbootwebfluxjjwt.password.encoder.keylength}") private Integer keyLength;

  @Override
  public String encode(CharSequence rawPassword) {
    try {
      byte[] result = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
          .generateSecret(new PBEKeySpec(rawPassword.toString().toCharArray(),
              secret.getBytes(),
              iteration,
              keyLength))
          .getEncoded();
      return Base64.getEncoder().encodeToString(result);
    } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    return encode(rawPassword).equals(encodedPassword);
  }
}
