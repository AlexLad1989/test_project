package com.github.xini1.exception;

/**
 * @author Alexandr Ladoshkin
 */
public final class FilmNameRequired extends RuntimeException {

    public FilmNameRequired() {
        super("Film name to search for is required");
    }
}
