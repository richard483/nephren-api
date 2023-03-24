package com.example.metanephren.services;

import com.example.metanephren.repositories.ImageGridFsRepository;
import com.example.metanephren.responses.MetaNephrenBaseResponse;
import com.example.metanephren.securities.JWTUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsTemplate;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@Slf4j
public class ImageServices {
  private final ReactiveGridFsTemplate gridFsTemplate;
  private final JWTUtil jwtUtil;
  private final ImageGridFsRepository imageGridFsRepository;

  @Autowired
  public ImageServices(ReactiveGridFsTemplate gridFsTemplate,
      JWTUtil jwtUtil,
      ImageGridFsRepository imageGridFsRepository) {
    this.gridFsTemplate = gridFsTemplate;
    this.jwtUtil = jwtUtil;
    this.imageGridFsRepository = imageGridFsRepository;
  }

  public Mono<MetaNephrenBaseResponse<Object>> uploadImage(String name,
      String desc,
      Mono<FilePart> file,
      String token) {
    String authenticatedUsername = jwtUtil.getUsernameFromToken(token);
    DBObject metaData = new BasicDBObject();
    metaData.put("name", name);
    metaData.put("desc", desc);
    metaData.put("uploader", authenticatedUsername);
    return file.flatMap(filePart -> gridFsTemplate.store(filePart.content(),
            filePart.name(),
            metaData))
        .map(id -> MetaNephrenBaseResponse.builder()
            .body(Map.of("id",
                id.toHexString(),
                "name",
                name,
                "desc",
                desc,
                "uploader",
                authenticatedUsername))
            .success(true)
            .build());
  }

  public Mono<Void> getImageFromId(String id, ServerWebExchange exchange) {
    return gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)))
        .log()
        .flatMap(gridFsTemplate::getResource)
        .flatMap(r -> exchange.getResponse().writeWith(r.getDownloadStream()));
  }

  public Mono<MetaNephrenBaseResponse<Object>> getImageInformationFromId(String id) {
    return gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id))).map(file -> {
      Document metadata = file.getMetadata();
      return MetaNephrenBaseResponse.builder()
          .body(Map.of(file.getId().asString(),
              id,
              "name",
              metadata.getString("name"),
              "desc",
              metadata.getString("desc"),
              "uploader",
              metadata.getString("uploader")))
          .success(true)
          .build();
    });
  }

  public Mono<MetaNephrenBaseResponse<Object>> getImagesList(Integer page, Integer contentPerPage) {
    return imageGridFsRepository.findAll(Sort.by("uploadDate").ascending(), page, contentPerPage)
        .collectList()
        .map(imageGridFs -> MetaNephrenBaseResponse.builder()
            .body(imageGridFs)
            .success(true)
            .build());
  }

  public Mono<MetaNephrenBaseResponse<Object>> getAllImageInformationList() {
    return gridFsTemplate.find(new Query(Criteria.where("_id").exists(true)).with(Sort.by(
            "uploadDate").ascending()))
        .map(fl -> fl.getMetadata().append("id", fl.getId().toString().substring(19, 43)))
        .collectList()
        .map(dataList -> MetaNephrenBaseResponse.builder().body(dataList).build());
  }

}
