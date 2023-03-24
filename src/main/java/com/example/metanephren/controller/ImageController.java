package com.example.metanephren.controller;

import com.example.metanephren.responses.MetaNephrenBaseResponse;
import com.example.metanephren.services.ImageServices;
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
  ImageServices imageServices;

  @Autowired
  public ImageController(ImageServices imageServices) {
    this.imageServices = imageServices;
  }

  @PostMapping("/add")
  @PreAuthorize("hasRole('MEMBER')")
  public Mono<MetaNephrenBaseResponse<Object>> uploadPhoto(@RequestParam String title,
      @RequestParam String desc,
      @RequestHeader("Authorization") String token,
      @RequestPart Mono<FilePart> image) {
    return imageServices.uploadImage(title, desc, image, token);
  }

  @GetMapping("/{id}")
  public Mono<Void> getPhoto(@PathVariable String id, ServerWebExchange serverWebExchange) {
    return imageServices.getImageFromId(id, serverWebExchange);
  }

  @GetMapping("/info/{id}")
  public Mono<MetaNephrenBaseResponse<Object>> getPhotoInfo(@PathVariable String id) {
    return imageServices.getImageInformationFromId(id);
  }

  @GetMapping("/info/page")
  public Mono<MetaNephrenBaseResponse<Object>> getImageList(@RequestParam Integer page,
      @RequestParam Integer contentPerPage) {
    return imageServices.getImagesList(page, contentPerPage);
  }

  @GetMapping("/info/all")
  public Mono<MetaNephrenBaseResponse<Object>> getAllImageInfoList() {
    return imageServices.getAllImageInformationList();
  }

}
