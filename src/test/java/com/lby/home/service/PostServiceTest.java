package com.lby.home.service;

import com.lby.home.domain.Post;
import com.lby.home.exception.PostNotFound;
import com.lby.home.repository.PostRepository;
import com.lby.home.request.PostCreate;
import com.lby.home.request.PostEdit;
import com.lby.home.request.PostSearch;
import com.lby.home.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void beforeEach() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1() {
        final PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        postService.write(postCreate);

        assertEquals(1L, postRepository.count());
        final Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test2() {

        final Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();

        postRepository.save(requestPost);

        final PostResponse response = postService.get(requestPost.getId());

        assertNotNull(response);
        assertEquals(1L, postRepository.count());
        assertEquals("foo", response.getTitle());
        assertEquals("bar", response.getContent());
    }

    @Test
    @DisplayName("글 여러개 조회")
    void test3() {
        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> Post.builder()
                        .title("foo" + i)
                        .content("bar1" + i)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

       // Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "id"));

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .size(10)
                .build();

        final List<PostResponse> posts = postService.getList(postSearch);

        assertEquals(10L, posts.size());
        assertEquals("foo19", posts.get(0).getTitle());
    }

    @Test
    @DisplayName("글 제목 수정")
    void test4() {
        // given
        final Post post = Post.builder()
                .title("lby")
                .content("가산")
                .build();
        postRepository.save(post);

        final PostEdit postEdit = PostEdit.builder()
                .title("ybl")
                .content("가산")
                .build();

        // when
        postService.edit(post.getId(), postEdit);

        // then
        final Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + post.getId()));
        Assertions.assertEquals("ybl", changedPost.getTitle());
        Assertions.assertEquals("가산", changedPost.getContent());

    }

    @Test
    @DisplayName("글 내용 수정")
    void test5() {
        // given
        final Post post = Post.builder()
                .title("lby")
                .content("가산")
                .build();
        postRepository.save(post);

        final PostEdit postEdit = PostEdit.builder()
                .title("lby")
                .content("구로")
                .build();

        // when
        postService.edit(post.getId(), postEdit);

        // then
        final Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + post.getId()));
        Assertions.assertEquals("lby", changedPost.getTitle());
        Assertions.assertEquals("구로", changedPost.getContent());

    }

    @Test
    @DisplayName("게시글 삭제")
    void test6() {
        // given
        final Post post = Post.builder()
                .title("lby")
                .content("가산")
                .build();
        postRepository.save(post);

        // when
        postService.delete(post.getId());

        // then
        Assertions.assertEquals(0, postRepository.count());
    }

    @Test
    @DisplayName("글 1개 조회 - 존재하지 않는 글")
    void test7() {
        // given
        final Post post = Post.builder()
                .title("lby")
                .content("가산")
                .build();
        postRepository.save(post);

        // expected
        assertThrows(PostNotFound.class, () -> {
            postService.get(post.getId() + 1L);
        });
    }

    @Test
    @DisplayName("게시글 삭제 - 존재하지 않는 글")
    void test8() {
        // given
        final Post post = Post.builder()
                .title("lby")
                .content("가산")
                .build();
        postRepository.save(post);

        // expected
        assertThrows(PostNotFound.class, () -> {
            postService.delete(post.getId() + 1L);
        });
    }

    @Test
    @DisplayName("글 내용 수정 - 존재하지 않는 글")
    void test9() {
        // given
        final Post post = Post.builder()
                .title("lby")
                .content("가산")
                .build();
        postRepository.save(post);

        final PostEdit postEdit = PostEdit.builder()
                .title("lby")
                .content("구로")
                .build();
        System.out.println("postEdit = " + postEdit);
        // expected
        assertThrows(PostNotFound.class, () -> {
            postService.edit(post.getId() + 1L, postEdit);
        });

    }
}
