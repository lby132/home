package com.lby.home.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * {
 *     "code":"400",
 *     "message":"잘못된 요청입니다.",
 *     "validation" : {
 *         "title" : "값을 입력해주세요"
 *     }
 * }
 */
@Data
@RequiredArgsConstructor
public class ErrorResponse {

    private final String code;
    private final String message;

    private final Map<String, String> validation = new HashMap<>();

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }
}
