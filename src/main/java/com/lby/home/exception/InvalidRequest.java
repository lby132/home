package com.lby.home.exception;

import lombok.Getter;

/**
 * status -> 400
 */
@Getter
public class InvalidRequest extends LbyException {

    private static final String MESSAGE = "잘못된 요청입니다.";

    private String fieldName;
    private String message;

    public InvalidRequest() {
        super(MESSAGE);
    }

    public InvalidRequest(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
