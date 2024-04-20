package com.example.wallpapers.service;

import com.example.wallpapers.dto.PostDto;
import com.example.wallpapers.exception.NotFoundException;
import com.example.wallpapers.model.Post;
import com.example.wallpapers.repository.PostRepository;
import com.example.wallpapers.utils.MappingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<PostDto> getAll(int page, int size, String sortByField) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortByField));
        return postRepository.findAll(pageable).getContent()
                .stream().map(MappingUtils::mapToPostDto).toList();
    }

    public PostDto getById(Long id) {
        return MappingUtils.mapToPostDto(
                postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post not found"))
        );
    }

    public void create(Post post, Long userId) {

    }

    public void update() {

    }

}
