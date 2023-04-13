package com.example.metanephren.services;

import com.example.metanephren.models.responses.MetaNephrenBaseResponse;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public interface ImageService {
  Mono<MetaNephrenBaseResponse<Object>> uploadImage(String name,
      String desc,
      Mono<FilePart> file,
      String token);

  Mono<Void> getImageFromId(String id, ServerWebExchange exchange);

  Mono<MetaNephrenBaseResponse<Object>> getImageInformationFromId(String id);

  Mono<MetaNephrenBaseResponse<Object>> getImagesList(Integer page, Integer contentPerPage);

  Mono<MetaNephrenBaseResponse<Object>> getAllImageInformationList();
}
