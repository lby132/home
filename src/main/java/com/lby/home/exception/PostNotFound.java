package com.lby.home.exception;

/**
 * status -> 404
 */
public class PostNotFound extends LbyException {

    private static final String message = "존재하지 않는 글입니다.";

    public PostNotFound() {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
