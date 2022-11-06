package com.lby.home.service;

import com.lby.home.domain.Post;
import com.lby.home.domain.PostEditor;
import com.lby.home.exception.PostNotFound;
import com.lby.home.repository.PostRepository;
import com.lby.home.request.PostCreate;
import com.lby.home.request.PostEdit;
import com.lby.home.request.PostSearch;
import com.lby.home.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreate postCreate) {

        final Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();

        postRepository.save(post);
    }

    public PostResponse get(Long id) {
        final Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFound());
                //.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    public List<PostResponse> getList(PostSearch postSearch) {
        return postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void edit(Long id, PostEdit postEdit) {
        final Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        final PostEditor.PostEditorBuilder postEditorBuilder = post.toEditor();

        final PostEditor postEditor = postEditorBuilder
                .title(postEdit.getTitle())
                .content(postEdit.getContent())
                .build();

        post.edit(postEditor);

    }

    public void delete(Long id) {
        final Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);
        postRepository.delete(post);
    }
}
