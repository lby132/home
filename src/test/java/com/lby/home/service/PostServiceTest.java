package com.lby.home.service;

import com.lby.home.request.PostCreate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
public class PostServiceTest {

    @Test
    @DisplayName("글 작성")
    void test1() {
        PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
    }
}
