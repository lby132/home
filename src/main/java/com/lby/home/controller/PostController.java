package com.lby.home.controller;

import com.lby.home.config.data.UserSession;
import com.lby.home.request.PostCreate;
import com.lby.home.request.PostEdit;
import com.lby.home.request.PostSearch;
import com.lby.home.response.PostResponse;
import com.lby.home.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/foo")
    public String foo(UserSession userSession) {
        log.info(">>>{}", userSession.name);
        return "foo";
    }

    @PostMapping("/posts")
    public void posts(@RequestBody @Valid PostCreate request) throws Exception {
        request.validate();
        postService.write(request);
    }

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId) {
        return postService.get(postId);
    }

    @GetMapping("/posts")
    public List<PostResponse> getList(PostSearch postSearch) {
        // @PageableDefault = get파라미터로 페이지번호가 넘어왔을때 보정 처리를 해준다. 예를 들면 get으로 페이지 1로 들어오면 0으로 처리해준다.
        return postService.getList(postSearch);
    }

    @PatchMapping("/posts/{postId}")
    public void edit(@PathVariable Long postId, @RequestBody @Valid PostEdit request) {
        postService.edit(postId, request);
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }
}
