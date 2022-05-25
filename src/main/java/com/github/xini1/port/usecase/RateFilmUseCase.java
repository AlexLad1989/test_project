package com.github.xini1.port.usecase;

/**
 * @author Alexandr Ladoshkin
 */
public interface RateFilmUseCase {

    void rate(String apiToken, String imdbId, int rating);
}
