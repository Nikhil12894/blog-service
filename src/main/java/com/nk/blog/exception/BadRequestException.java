package com.nk.blog.exception;

import java.util.List;

public class BadRequestException extends RuntimeException {
    private List<String> errors;

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(List<String> errors) {
        this.errors = errors;
    }

    public BadRequestException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
