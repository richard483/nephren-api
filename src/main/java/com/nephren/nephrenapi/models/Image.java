package com.nephren.nephrenapi.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image {
  private String id;
  private String name;
  private String desc;
  private String uploader;
}
