package ru.rosbank.javaschool.crudapi.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.rosbank.javaschool.crudapi.dto.PostResponseDto;
import ru.rosbank.javaschool.crudapi.dto.PostSaveRequestDto;
import ru.rosbank.javaschool.crudapi.exception.BadRequestException;
import ru.rosbank.javaschool.crudapi.model.PostModel;
import ru.rosbank.javaschool.crudapi.repository.PostRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
  private final PostRepository repository;
  private final Logger logger = LoggerFactory.getLogger(PostService.class);
  private int firstSeenPostId = 0;
  private int countPostsToShow = -1;
  private final int countPostsByOneLoad = 5;

  public List<PostResponseDto> getMore() {
    countPostsToShow += countPostsByOneLoad;
    return getAll();
  }

  public List<PostResponseDto> getNews() {
    int tmpFirstSeenPostId = repository.getAllNoRemoved().get(repository.getAllNoRemoved().size() - 1).getId();
    if (tmpFirstSeenPostId != firstSeenPostId) {
      firstSeenPostId = tmpFirstSeenPostId;
      countPostsToShow = 5;
    }
    return getAll();
  }

  public List<PostResponseDto> getAll() {
    List<PostModel> allPostModels = repository.getAllNoRemoved();
    List<PostModel> result = new ArrayList<>();
    Collections.reverse(allPostModels);
    if (countPostsToShow < 0) {
      int lastIdInRepo = allPostModels.get(0).getId();
      firstSeenPostId = lastIdInRepo;
      countPostsToShow = countPostsByOneLoad;
    }

    for (PostModel post : allPostModels) {
      if (post.getId() > firstSeenPostId) {
        continue;
      }
      result.add(post);
    }

    if (result.size() < countPostsToShow) {
      return result.stream()
            .map(PostResponseDto::from)
            .collect(Collectors.toList());
    }
    return result.subList(0, countPostsToShow).stream()
            .map(PostResponseDto::from)
            .collect(Collectors.toList());
  }

  public PostResponseDto save(PostSaveRequestDto dto) {
    logger.info(dto.toString());
    return repository.save(PostModel.from(dto))
        .map(PostResponseDto::from)
        .orElseThrow(BadRequestException::new);
  }

  public void removeById(int id) {
    repository.removeById(id);
  }

  public List<PostResponseDto> searchByContent(String q) {
    return repository.searchByContentNotRemoved(q).stream()
        .map(PostResponseDto::from)
        .collect(Collectors.toList());
  }

  public PostResponseDto likeById(int id) {
    return repository.likeById(id)
        .map(PostResponseDto::from)
        .orElseThrow(BadRequestException::new);
  }

  public PostResponseDto dislikeById(int id) {
    return repository.dislikeById(id)
        .map(PostResponseDto::from)
        .orElseThrow(BadRequestException::new);
  }

  public int getCountNews() {
    return repository.getAllNoRemoved().get(repository.getAllNoRemoved().size() - 1).getId() - firstSeenPostId;
  }

}
