package com.lby.home.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lby.home.domain.Post;
import com.lby.home.repository.PostRepository;
import com.lby.home.request.PostCreate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.lby.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
public class PostControllerDocTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
//                .apply(documentationConfiguration(restDocumentation))
//                .build();
//    }

    @Test
    @DisplayName("??? ?????? ??????")
    void test1() throws Exception {
        // given
        Post post = Post.builder()
                .title("??????")
                .content("??????")
                .build();
        postRepository.save(post);

        // expected
        mockMvc.perform(RestDocumentationRequestBuilders.get("/posts/{postId}", 1L)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("index", RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("postId").description("????????? ID")
                        ),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("id").description("????????? ID"),
                                PayloadDocumentation.fieldWithPath("title").description("??????"),
                                PayloadDocumentation.fieldWithPath("content").description("??????")
                        )
                ));
    }

    @Test
    @DisplayName("??? ??????")
    void test2() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("lby")
                .content("??????")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-create",
                        requestFields(
                                fieldWithPath("title").description("??????")
                                .attributes(key("constraint").value("???????????? ??????????????????.")),
                                fieldWithPath("content").description("??????").optional()
                        )
                ));
    }
}
