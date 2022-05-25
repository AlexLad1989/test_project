package com.github.xini1.port.usecase;

/**
 * @author Alexandr Ladoshkin
 */
public interface ListTopRatedFilmsUseCase {

    Page topRatedSortedByBoxOffice(String apiToken, int page, int elementsOnPage);
}
