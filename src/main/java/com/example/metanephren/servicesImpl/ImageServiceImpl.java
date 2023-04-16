package com.example.metanephren.servicesImpl;

import com.example.metanephren.models.Image;
import com.example.metanephren.models.ImageGridFs;
import com.example.metanephren.repositories.ImageGridFsRepository;
import com.example.metanephren.securities.JWTUtil;
import com.example.metanephren.services.ImageService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsTemplate;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {
  private final ReactiveGridFsTemplate gridFsTemplate;
  private final JWTUtil jwtUtil;
  private final ImageGridFsRepository imageGridFsRepository;

  @Autowired
  public ImageServiceImpl(ReactiveGridFsTemplate gridFsTemplate,
      JWTUtil jwtUtil,
      ImageGridFsRepository imageGridFsRepository) {
    this.gridFsTemplate = gridFsTemplate;
    this.jwtUtil = jwtUtil;
    this.imageGridFsRepository = imageGridFsRepository;
  }

  @Override
  public Mono<Image> uploadImage(String name,
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
        .map(id -> Image.builder()
            .id(id.toHexString())
            .name(name)
            .desc(desc)
            .uploader(authenticatedUsername)
            .build());
  }

  @Override
  public Mono<Void> getImageFromId(String id, ServerWebExchange exchange) {
    return gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)))
        .log()
        .flatMap(gridFsTemplate::getResource)
        .flatMap(r -> exchange.getResponse().writeWith(r.getDownloadStream()));
  }

  @Override
  public Mono<GridFSFile> getImageInformationFromId(String id) {
    return gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
  }

  /**
   * @param page           index, starts from 0
   * @param contentPerPage the number of image items per page
   * @return list of items that already arranged per pages
   */
  @Override
  public Flux<ImageGridFs> getImagesList(Integer page, Integer contentPerPage) {
    return imageGridFsRepository.findAll(Sort.by("uploadDate").ascending(), page, contentPerPage);
  }

  @Override
  public Flux<GridFSFile> getAllImageInformationList() {
    return gridFsTemplate.find(new Query(Criteria.where("_id").exists(true)).with(Sort.by(
        "uploadDate").ascending()));
  }

}
