package com.example.metanephren.services;

import com.example.metanephren.responses.MetaNephrenBaseResponse;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  public ImageServices(ReactiveGridFsTemplate gridFsTemplate) {
    this.gridFsTemplate = gridFsTemplate;
  }

  public Mono<MetaNephrenBaseResponse<Object>> uploadImage(String name,
      String desc,
      Mono<FilePart> file) {
    DBObject metaData = new BasicDBObject();
    metaData.put("name", name);
    metaData.put("desc", desc);
    return file.flatMap(filePart -> gridFsTemplate.store(filePart.content(),
            filePart.name(),
            metaData))
        .map(id -> MetaNephrenBaseResponse.builder()
            .body(Map.of("id", id.toHexString(), "name", name, "desc", desc))
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
          .body(Map.of("id",
              metadata.getString("id"),
              "name",
              metadata.getString("name"),
              "desc",
              metadata.getString("desc")))
          .success(true)
          .build();
    });
  }
}
