package com.example.metanephren.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("Images.files")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageGridFs {
  @Id
  private String id;
  private String filename;
  private Date uploadDate;
}
