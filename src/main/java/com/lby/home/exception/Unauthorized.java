package com.lby.home.exception;

public class Unauthorized extends LbyException {

    private static final String message = "인증이 필요합니다.";

    public Unauthorized() {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
