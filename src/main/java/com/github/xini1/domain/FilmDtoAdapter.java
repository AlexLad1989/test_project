package com.github.xini1.domain;

import com.github.xini1.port.FilmDescriptions;
import com.github.xini1.port.usecase.FilmDto;

import java.util.Objects;

/**
 * @author Alexandr Ladoshkin
 */
final class FilmDtoAdapter extends FilmDto {

    private final FilmDescriptions.FilmDescription filmDescription;
    private final boolean oscarWinner;
    private final int rating;

    FilmDtoAdapter(FilmDescriptions.FilmDescription filmDescription, boolean oscarWinner, int rating) {
        this.filmDescription = Objects.requireNonNull(filmDescription);
        this.oscarWinner = oscarWinner;
        this.rating = rating;
    }

    @Override
    public String imdbId() {
        return filmDescription.imdbId();
    }

    @Override
    public String name() {
        return filmDescription.name();
    }

    @Override
    public boolean isOscarWinner() {
        return oscarWinner;
    }

    @Override
    public int rating() {
        return rating;
    }

    @Override
    public int boxOffice() {
        return filmDescription.boxOffice();
    }
}
