package ru.rosbank.javaschool.crudapi.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.rosbank.javaschool.crudapi.dto.PostSaveRequestDto;
import ru.rosbank.javaschool.crudapi.dto.PostResponseDto;
import ru.rosbank.javaschool.crudapi.service.PostService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class RestPostController {
  private final PostService service;
  private final Logger logger = LoggerFactory.getLogger(RestPostController.class);

  @GetMapping
  public List<PostResponseDto> getAll() {
    logger.info(Thread.currentThread().getName());
    return service.getAll();
  }

  @GetMapping(params = "more")
  public List<PostResponseDto> getMore() {
    logger.info(Thread.currentThread().getName());
    return service.getMore();
  }

  @GetMapping(params = "news")
  public List<PostResponseDto> getNews() {
    logger.info(Thread.currentThread().getName());
    return service.getNews();
  }

  @GetMapping(params = "count-news")
  public int getCountNews() {
    logger.info(Thread.currentThread().getName());
    return service.getCountNews();
  }

  @GetMapping(params = "q")
  public List<PostResponseDto> searchByContent(@RequestParam String q) {
    return service.searchByContent(q);
  }

  @PostMapping
  public PostResponseDto save(@RequestBody PostSaveRequestDto dto) {
    return service.save(dto);
  }

  @DeleteMapping("/{id}")
  public void removeById(@PathVariable int id) {
    service.removeById(id);
  }

  @PostMapping("/{id}/likes")
  public PostResponseDto likeById(@PathVariable int id) {
    return service.likeById(id);
  }

  @DeleteMapping("/{id}/likes")
  public PostResponseDto dislikeById(@PathVariable int id) {
    return service.dislikeById(id);
  }

}
