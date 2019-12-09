package ru.rosbank.javaschool.crudapi.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadInfo {
  private Resource resource;
  private String mimeType;
}
