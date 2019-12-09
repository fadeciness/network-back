package ru.rosbank.javaschool.crudapi.service;

import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.rosbank.javaschool.crudapi.domain.UploadInfo;
import ru.rosbank.javaschool.crudapi.dto.UploadRequestDto;
import ru.rosbank.javaschool.crudapi.dto.UploadResponseDto;
import ru.rosbank.javaschool.crudapi.exception.FileStorageException;
import ru.rosbank.javaschool.crudapi.exception.UnsupportedFileTypeException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {
  private final static Logger logger = LoggerFactory.getLogger(FileService.class);
  private final String uploadPath;

  public FileService(@Value("${app.upload-path}") String uploadPath) {
    logger.info(uploadPath);
    this.uploadPath = uploadPath;
  }

  @PostConstruct
  public void init() throws IOException {
    Path path = Paths.get(uploadPath);
    if (Files.notExists(path)) {
      Files.createDirectories(path);
    }
  }

  public UploadResponseDto save(MultipartFile multipartFile) {
    String extension;
    String contentType = multipartFile.getContentType();
    if ("image/jpeg".equals(contentType)) {
      extension = ".jpg";
    } else if ("image/png".equals(contentType)) {
      extension = ".png";
    } else if ("video/webm".equals(contentType)) {
      extension = ".webm";
    } else {
      throw new UnsupportedFileTypeException(contentType);
    }

    String name = UUID.randomUUID() + extension;
    try {
      multipartFile.transferTo(Paths.get(uploadPath).resolve(name));
      return new UploadResponseDto(name);
    } catch (IOException e) {
      throw new FileStorageException(e);
    }
  }

  public UploadInfo get(String id) {
    try {
      return new UploadInfo(
          new FileSystemResource(Paths.get(uploadPath).resolve(id)),
          Files.probeContentType(Paths.get(uploadPath).resolve(id))
      );
    } catch (IOException e) {
      throw new FileStorageException(e);
    }
  }

  public UploadResponseDto save(UploadRequestDto dto) {
    String extension;
    String contentType = dto.getFile().getContentType();
    if ("image/jpeg".equals(contentType)) {
      extension = ".jpg";
    } else if ("image/png".equals(contentType)) {
      extension = ".png";
    } else {
      throw new UnsupportedFileTypeException(contentType);
    }

    String name = dto.getName() + extension;
    try {
      dto.getFile().transferTo(Paths.get(uploadPath).resolve(name));
      return new UploadResponseDto(name);
    } catch (IOException e) {
      throw new FileStorageException(e);
    }
  }

  public UploadResponseDto save(byte[] bytes) {
    Tika tika = new Tika();
    String contentType = tika.detect(bytes);
    String extension;
    if ("image/jpeg".equals(contentType)) {
      extension = ".jpg";
    } else if ("image/png".equals(contentType)) {
      extension = ".png";
    } else if ("video/webm".equals(contentType)) {
      extension = ".webm";
    } else {
      throw new UnsupportedFileTypeException(contentType);
    }

    String name = UUID.randomUUID() + extension;
    try {
      Files.write(Paths.get(uploadPath).resolve(name), bytes);
      return new UploadResponseDto(name);
    } catch (IOException e) {
      throw new FileStorageException(e);
    }
  }

}
