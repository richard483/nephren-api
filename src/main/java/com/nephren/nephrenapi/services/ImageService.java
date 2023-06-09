package com.nephren.nephrenapi.services;

import com.nephren.nephrenapi.models.Image;
import com.nephren.nephrenapi.models.ImageGridFs;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ImageService {
  Mono<Image> uploadImage(String name,
      String desc,
      Mono<FilePart> file,
      String token);

  Mono<Void> getImageFromId(String id, ServerWebExchange exchange);

  Mono<GridFSFile> getImageInformationFromId(String id);

  Flux<ImageGridFs> getImagesList(Integer page, Integer contentPerPage);

  Flux<GridFSFile> getAllImageInformationList();
}
