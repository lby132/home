package com.lby.home.controller;

import com.lby.home.request.PostCreate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class PostController {

    @PostMapping("/posts")
    public String post(PostCreate param) {
        log.info("params={}", param);
        return "Hello World";
    }
}
