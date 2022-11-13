package com.lby.home.controller;

import com.lby.home.request.PostCreate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class exController {

  //  @PostMapping("/posts")
    public String post(@RequestBody PostCreate param) throws Exception {
        log.info("params={}", param.toString());

        final String title = param.getTitle();
        if (title == null || title.equals("")) {
            throw new Exception("타이틀값이 없어요");
        }

        final String content = param.getContent();
        if (content == null || content.equals("")) {

        }

        return "Hello World";
    }

    // @Vaild 검증 어노테이션 사용
  //  @PostMapping("/posts")
    public String post2(@RequestBody @Valid PostCreate param, BindingResult result) throws Exception {
        log.info("params={}", param.toString());
        return "Hello World";
    }

    // 클라이언트에 어떤 오류인지 메세지로 내려줌
    //@PostMapping("/posts")
    public Map<String, String> post3(@RequestBody @Valid PostCreate param, BindingResult result) throws Exception {
        System.out.println("param = " + param);
        if (result.hasErrors()) {
            final List<FieldError> fieldErrors = result.getFieldErrors();
            final FieldError firstFieldError = fieldErrors.get(0);
            final String fieldName = firstFieldError.getField(); //title
            final String errorMessage = firstFieldError.getDefaultMessage();

            final Map<String, String> error = new HashMap<>();
            error.put(fieldName, errorMessage);
            return error;
        }
        return Map.of();
    }

    //@PostMapping("/posts")
    public Map<String, String> post4(@RequestBody @Valid PostCreate request) throws Exception {
        return Map.of();
    }
}
