package com.lby.home.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lby.home.domain.Post;
import com.lby.home.repository.PostRepository;
import com.lby.home.request.PostCreate;
import com.lby.home.request.PostEdit;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;


// Controller layer만 테스트를 하면 @WebMvcTest만 써주면 되는데
//Service, Repository를 탄다면 @SpringBootTest를 써줘야함.
@SpringBootTest
// mockMvc에 빈이 주입되어서 사용할 수 있다. 이 어노테이션 없으면 mocMvc에 값이 없어서 에러남
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach // 이거 없으면 인서트가 두군데에서 들어간다면 데이터가 쌓여서 제대로된 결과가 안나온다.
        // 테스트 하나 실행하고 지우고 하나실행하고 지우기 위해 추가함.
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("/posts 요청시 Hello World를 출력한다.")	//테스트 이름 작성하는곳
    void test() throws Exception {

        PostCreate request = PostCreate.builder()		//@Builder패턴. 생성자 주입도 좋지만 파라미터의 순서가 바뀌거나 할 수 있어서 안전하게 builder로 사용 그리고 불변객체로 사용할 수 있다는게 장점.
                .title("제목입니다.")	// PostCreate라는 클래스에서 만든 필드명이 title과 content이다.
                .content("내용입니다.")	// builer를 사용한 객체에 클래스나 메서드 위에 @Builder를 달아주면 되는데 클래스레벨에 달면 모든 메서드들이 적용되기 때문에 필요한 메서드에만 다는걸 추천.
                .build();

        ObjectMapper objectMapper = new ObjectMapper();		// 아시다시피 json을 자바로 deserialization 하거나 serialization할때 사용하는 json라이브러리의 클래스가 ObjectMapper.class
        String json = objectMapper.writeValueAsString(request); // json을 쓸때. json을 읽을땐 readValue였던거 같은데..

        System.out.println("json = " + json);

        mockMvc.perform(MockMvcRequestBuilders.post("/posts")		// @PostMapping("/posts")
                                .contentType(MediaType.APPLICATION_JSON)	// json으로 받기위한 컨텐트타입지정.
                                //결과에 Headers = [Content-Type:"application/json;charset=UTF-8", Content-Length:"57"]
                                //로 나감. Content-Length는 내가 설정안했음. Dispatcher Servlet이 알어서 컨텐트갯수세서 해주는걸로 알고 있음.
                                .content(json)
                        // Body에 {"title":"제목입니다.","content":"내용입니다."} 이런식으로 json문자열로 결과가 나감.
                        // readValue를 하면 json객체로 나감.
                )
                // andExpect는 응답을 검증한다.
                .andExpect(MockMvcResultMatchers.status().isOk())	// 결과를 받았을때 상태값을 200으로 설정
                .andExpect(MockMvcResultMatchers.content().string(""))	// 목 객체에서 받은 결과값이 빈값이면.
                .andDo(MockMvcResultHandlers.print()); // 요청/응답 전체 메세지를 확인할수 있다.
    }	             // 참고로 MockMvcResultHandlers나 이런것들 static import가능해서 더 깔끔하게 쓸수있다. 공부용이라 그냥 남겨뒀다.

    @Test
    @DisplayName("/posts 요청시 title값은 필수다.")
    void test2() throws Exception {

        final PostCreate request = PostCreate.builder()
                .content("내용입니다.")
                .build();

        final String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("/posts 요청시 DB에 값이 저장된다.")
    void test3() throws Exception {

        final PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        final String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                        //   .content("{\"title\": \"제목입니다.\", \"content\": \"내용입니다.\"}")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        Assertions.assertEquals(1L, postRepository.count());

        final Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("")
    void test4() throws Exception {
        final Post post = Post.builder()
                .title("1234567890")
                .content("bar")
                .build();
        postRepository.save(post);

        // excepted
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(post.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("1234567890"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("bar"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("페이지를 0으로 요청하면 첫 페이지를 가져온다")
    void test5() throws Exception {
        // given
        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> Post.builder()
                        .title("foo" + i)
                        .content("bar" + i)
                        .build())
                        .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        // excepted
        mockMvc.perform(MockMvcRequestBuilders.get("/posts?page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("foo19"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].content").value("bar19"))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("글 제목 수정")
    void test6() throws Exception {
        // given
        final Post post = Post.builder()
                .title("lby")
                .content("가산")
                .build();
        postRepository.save(post);

        final PostEdit postEdit = PostEdit.builder()
                .title("lby2")
                .content("구로")
                .build();

        // excepted
        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }
}
