package com.github.xini1.exception;

/**
 * @author Alexandr Ladoshkin
 */
public final class ApiTokenRequired extends RuntimeException {

    public ApiTokenRequired() {
        super("API token is required");
    }
}
