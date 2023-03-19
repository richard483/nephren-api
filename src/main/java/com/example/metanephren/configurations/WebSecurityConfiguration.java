package com.example.metanephren.configurations;

import com.example.metanephren.securities.AuthenticateManager;
import com.example.metanephren.securities.SecurityContextRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Configuration
public class WebSecurityConfiguration {

  private AuthenticateManager authenticateManager;
  private SecurityContextRepository securityContextRepository;

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    return http.exceptionHandling()
        .authenticationEntryPoint((swe, e) -> Mono.fromRunnable(() -> swe.getResponse()
            .setStatusCode(HttpStatus.UNAUTHORIZED)))
        .accessDeniedHandler((swe, e) -> Mono.fromRunnable(() -> swe.getResponse()
            .setStatusCode(HttpStatus.FORBIDDEN)))
        .and()
        .csrf()
        .disable()
        .formLogin()
        .disable()
        .httpBasic()
        .disable()
        .authenticationManager(authenticateManager)
        .securityContextRepository(securityContextRepository)
        .authorizeExchange()
        .pathMatchers(HttpMethod.OPTIONS)
        .permitAll()
        .pathMatchers("/login")
        .permitAll()
        .pathMatchers("/register")
        .permitAll()
        .anyExchange()
        .authenticated()
        .and()
        .build();
  }
}
