package com.github.xini1.port.usecase;

/**
 * @author Alexandr Ladoshkin
 */
public interface SearchFilmUseCase {

    Page search(String apiToken, String name, int page);
}
