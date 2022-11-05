package com.lby.home.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class PostEdit {

    @NotBlank(message = "타이틀을 입력해주세요.")
    private String title;

    @NotBlank(message = "콘텐츠를 입력해주세요.")
    private String content;

    @Builder
    public PostEdit(String title, String content) {
        this.title = title != null ? title : "제목이 없습니다.";
        this.content = content != null ? content : "내용이 없습니다.";
    }
}
