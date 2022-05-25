package com.github.xini1.exception;

/**
 * @author Alexandr Ladoshkin
 */
public final class FilmNotFound extends RuntimeException {

    public FilmNotFound(String imdbId) {
        super("Could not find film with IMDB ID " + imdbId);
    }
}
