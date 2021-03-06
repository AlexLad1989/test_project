package com.github.xini1.port;

import java.util.Collection;

/**
 * @author Alexandr Ladoshkin
 */
public interface FilmDescriptions {

    Page byName(String apiToken, String name, int page);

    FilmDescription byId(String apiToken, String imdbId);

    boolean isNotExists(String apiToken, String imdbId);

    interface FilmDescription {

        String imdbId();

        String name();

        int boxOffice();
    }

    interface Page {

        Collection<FilmDescription> filmDescriptions();

        int page();

        int totalPages();
    }
}
