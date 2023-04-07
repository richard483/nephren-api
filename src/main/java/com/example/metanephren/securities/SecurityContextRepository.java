package com.example.metanephren.securities;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@AllArgsConstructor
@Component
public class SecurityContextRepository implements ServerSecurityContextRepository {
  private AuthenticateManager authManager;

  @Override
  public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
    throw new UnsupportedOperationException("Not supported yet");
  }

  @Override
  public Mono<SecurityContext> load(ServerWebExchange exchange) {
    if (exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION) != null) {
      return Mono.just(Objects.requireNonNull(exchange.getRequest()
              .getHeaders()
              .getFirst(HttpHeaders.AUTHORIZATION)))
          .flatMap(header -> {
            Authentication auth = new UsernamePasswordAuthenticationToken(header, header);
            return this.authManager.authenticate(auth).map(SecurityContextImpl::new);
          });
    }
    return Mono.just("TOKEN").flatMap(header -> {
      Authentication auth = new UsernamePasswordAuthenticationToken(header, header);
      return this.authManager.authenticate(auth).map(SecurityContextImpl::new);
    });
  }
}
