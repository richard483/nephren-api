package com.example.metanephren.repositories;

import com.example.metanephren.models.ImageGridFs;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ImageGridFsRepository extends ReactiveMongoRepository<ImageGridFs, String> {
  default Flux<ImageGridFs> findAll(Sort sort, Integer page, Integer contentPerPage) {
    return findAll(sort).skip((long) page * contentPerPage).take(contentPerPage);
  }
}
