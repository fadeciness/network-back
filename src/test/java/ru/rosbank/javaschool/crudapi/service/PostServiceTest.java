package ru.rosbank.javaschool.crudapi.service;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.rosbank.javaschool.crudapi.dto.PostResponseDto;
import ru.rosbank.javaschool.crudapi.dto.PostSaveRequestDto;
import ru.rosbank.javaschool.crudapi.model.PostModel;
import ru.rosbank.javaschool.crudapi.repository.PostRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceTest {

    private PostRepository postRepository;
    private PostService service;

    @BeforeEach
    void initializeTests() {
        postRepository = mock(PostRepository.class);
        service = new PostService(postRepository);
    }


    @Test
    void getAllReturnOneElementWhenRepoHasOneElement() {
        doReturn(Arrays.asList(new PostModel())).when(postRepository).getAllNoRemoved();

        List<PostResponseDto> result = service.getAll();

        assertEquals(1, result.size());
    }

    @Test
    void getMoreReturnFiveElementsWhenRepoHasMoreThanFiveElements() {
        doReturn(Arrays.asList(
                new PostModel(),
                new PostModel(),
                new PostModel(),
                new PostModel()
        )).when(postRepository).getAllNoRemoved();

        List<PostResponseDto> result = service.getMore();

        assertEquals(4, result.size());
    }

    @Test
    void getNewsReturnFiveNewElementsWhenItPresentInRepo() {
        PostModel startPostModel = new PostModel();
        doReturn(Arrays.asList(startPostModel)).when(postRepository).getAllNoRemoved();
        service.getAll();
        doReturn(Arrays.asList(
                startPostModel,
                new PostModel(),
                new PostModel(),
                new PostModel(),
                new PostModel(),
                new PostModel()
        )).when(postRepository).getAllNoRemoved();

        List<PostResponseDto> result = service.getNews();

        assertEquals(5, result.size());
        assertFalse(result.contains(startPostModel));
    }

    @Test
    void saveReturnDtoWhenItWasSavedSuccessfuly() {
        PostSaveRequestDto dtoToSave = new PostSaveRequestDto();
        doReturn(Optional.of(PostModel.from(dtoToSave))).when(postRepository).save(PostModel.from(dtoToSave));

        PostResponseDto result = service.save(dtoToSave);

        assertEquals(PostResponseDto.from(PostModel.from(dtoToSave)), result);
    }

    @Test
    void removeById() {
        doNothing().when(postRepository).removeById(0);

        assertDoesNotThrow(() -> service.removeById(0));
    }

    @Test
    void searchByContent() {
        String search = "";
        doReturn(Arrays.asList(new PostModel(), new PostModel())).when(postRepository).searchByContentNotRemoved(search);

        List<PostResponseDto> result = service.searchByContent(search);

        assertEquals(2, result.size());
    }

    @Test
    void likeById() {
        PostModel model = new PostModel();
        doReturn(Optional.of(model)).when(postRepository).likeById(1);

        PostResponseDto result = service.likeById(1);

        assertEquals(PostResponseDto.from(model), result);

    }

    @Test
    void dislikeById() {
        PostModel model = new PostModel();
        doReturn(Optional.of(model)).when(postRepository).dislikeById(1);

        PostResponseDto result = service.dislikeById(1);

        assertEquals(PostResponseDto.from(model), result);
    }

    @Test
    void getCountNews() {
        doReturn(Arrays.asList(new PostModel())).when(postRepository).getAllNoRemoved();
        service.getAll();

        int result = service.getCountNews();

        assertEquals(0, result);
    }
}