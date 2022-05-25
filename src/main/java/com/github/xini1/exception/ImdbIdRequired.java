package com.github.xini1.exception;

/**
 * @author Alexandr Ladoshkin
 */
public final class ImdbIdRequired extends RuntimeException {

    public ImdbIdRequired() {
        super("IMDB ID is required");
    }
}
