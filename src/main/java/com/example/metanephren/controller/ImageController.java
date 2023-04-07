package com.example.metanephren.controller;

import com.example.metanephren.models.responses.MetaNephrenBaseResponse;
import com.example.metanephren.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/photo")
public class ImageController {
  ImageService imageService;

  @Autowired
  public ImageController(ImageService imageService) {
    this.imageService = imageService;
  }

  @PostMapping("/add")
  @PreAuthorize("hasRole('MEMBER')")
  public Mono<MetaNephrenBaseResponse<Object>> uploadPhoto(@RequestParam String title,
      @RequestParam String desc,
      @RequestHeader("Authorization") String token,
      @RequestPart Mono<FilePart> image) {
    return imageService.uploadImage(title, desc, image, token);
  }

  @GetMapping("/{id}")
  public Mono<Void> getPhoto(@PathVariable String id, ServerWebExchange serverWebExchange) {
    return imageService.getImageFromId(id, serverWebExchange);
  }

  @GetMapping("/info/{id}")
  public Mono<MetaNephrenBaseResponse<Object>> getPhotoInfo(@PathVariable String id) {
    return imageService.getImageInformationFromId(id);
  }

  @GetMapping("/info/page")
  public Mono<MetaNephrenBaseResponse<Object>> getImageList(@RequestParam Integer page,
      @RequestParam Integer contentPerPage) {
    return imageService.getImagesList(page, contentPerPage);
  }

  @GetMapping("/info/all")
  public Mono<MetaNephrenBaseResponse<Object>> getAllImageInfoList() {
    return imageService.getAllImageInformationList();
  }

}
