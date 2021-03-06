package com.github.xini1.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.xini1.InMemoryFilmDescriptions;
import com.github.xini1.exception.ApiTokenRequired;
import com.github.xini1.exception.ElementsOnPageLessThanZero;
import com.github.xini1.exception.FilmNotFound;
import com.github.xini1.exception.ImdbIdRequired;
import com.github.xini1.exception.IncorrectRating;
import com.github.xini1.exception.PageNumberLessThanZero;
import com.github.xini1.port.usecase.ListTopRatedFilmsUseCase;
import com.github.xini1.port.usecase.RateFilmUseCase;
import com.github.xini1.port.usecase.SearchFilmUseCase;
import org.junit.jupiter.api.Test;

/**
 * @author Alexandr Ladoshkin
 */
final class RateFilmUseCaseTest {

    private final RateFilmUseCase rateFilmUseCase;
    private final ListTopRatedFilmsUseCase listTopRatedFilmsUseCase;
    private final SearchFilmUseCase searchFilmUseCase;

    RateFilmUseCaseTest() {
        var configuration = new FilmsModuleConfiguration(
                new InMemoryFilmDescriptions(
                        new InMemoryFilmDescriptions.Stub("id1", "Rated1", 1),
                        new InMemoryFilmDescriptions.Stub("id2", "Rated2", 2),
                        new InMemoryFilmDescriptions.Stub("id3", "Rated3", 3),
                        new InMemoryFilmDescriptions.Stub("id4", "Rated4", 4),
                        new InMemoryFilmDescriptions.Stub("id5", "Unrated")
                ),
                new InMemoryOscarWinners(),
                new InMemoryRatings()
        );
        rateFilmUseCase = configuration.rateFilmUseCase();
        listTopRatedFilmsUseCase = configuration.listTopRatedFilmsUseCase();
        searchFilmUseCase = configuration.searchFilmUseCase();
    }

    @Test
    void givenUserRatedFilm_thenFilmHasExactRating() {
        rateFilmUseCase.rate("token", "id1", 10);

        assertThat(listTopRatedFilmsUseCase.topRatedSortedByBoxOffice("token", 0, 1))
                .isEqualTo(
                        new PageStub(
                                new FilmDtoStub("id1", "Rated1", false, 10, 4)
                        )
                );
    }

    @Test
    void givenTwoUsersRatedFilm_thenFilmHasAverageOfTwoRating() {
        rateFilmUseCase.rate("token1", "id1", 10);
        rateFilmUseCase.rate("token2", "id1", 4);

        assertThat(listTopRatedFilmsUseCase.topRatedSortedByBoxOffice("token1", 0, 1))
                .isEqualTo(
                        new PageStub(
                                new FilmDtoStub("id1", "Rated1", false, 7, 4)
                        )
                );
    }

    @Test
    void givenAverageRatingHasFractionPart_thenFilmHasRoundedRating() {
        rateFilmUseCase.rate("token1", "id1", 10);
        rateFilmUseCase.rate("token2", "id1", 5);

        assertThat(listTopRatedFilmsUseCase.topRatedSortedByBoxOffice("token1", 0, 1))
                .isEqualTo(
                        new PageStub(
                                new FilmDtoStub("id1", "Rated1", false, 8, 4)
                        )
                );
    }

    @Test
    void givenSeveralRatedFilms_thenFilmsOrderedByBoxOffice() {
        rateFilmUseCase.rate("token", "id1", 10);
        rateFilmUseCase.rate("token", "id2", 10);
        rateFilmUseCase.rate("token", "id3", 10);
        rateFilmUseCase.rate("token", "id4", 10);

        assertThat(listTopRatedFilmsUseCase.topRatedSortedByBoxOffice("token", 0, 4))
                .isEqualTo(
                        new PageStub(
                                new FilmDtoStub("id4", "Rated4", false, 10, 4),
                                new FilmDtoStub("id3", "Rated3", false, 10, 3),
                                new FilmDtoStub("id2", "Rated2", false, 10, 2),
                                new FilmDtoStub("id1", "Rated1", false, 10, 1)
                        )
                );
    }

    @Test
    void givenTotalElementsExceedElementsOnPage_thenReturnOnlySpecifiedNumberOfFilms() {
        rateFilmUseCase.rate("token", "id1", 10);
        rateFilmUseCase.rate("token", "id2", 10);
        rateFilmUseCase.rate("token", "id3", 10);
        rateFilmUseCase.rate("token", "id4", 10);

        var page = listTopRatedFilmsUseCase.topRatedSortedByBoxOffice("token", 0, 3);

        assertThat(page.films()).hasSize(3);
        assertThat(page.totalPages()).isEqualTo(2);
    }

    @Test
    void givenApiTokenIsNull_thenApiTokenRequiredThrown() {
        assertThatThrownBy(() -> rateFilmUseCase.rate(null, "whatever", 1))
                .isInstanceOf(ApiTokenRequired.class);
    }

    @Test
    void givenApiTokenIsEmpty_thenApiTokenRequiredThrown() {
        assertThatThrownBy(() -> rateFilmUseCase.rate("", "whatever", 1))
                .isInstanceOf(ApiTokenRequired.class);
    }

    @Test
    void givenImdbIdIsNull_thenImdbIdRequiredThrown() {
        assertThatThrownBy(() -> rateFilmUseCase.rate("token", null, 1))
                .isInstanceOf(ImdbIdRequired.class);
    }

    @Test
    void givenImdbIdIsEmpty_thenImdbIdRequiredThrown() {
        assertThatThrownBy(() -> rateFilmUseCase.rate("token", "", 1))
                .isInstanceOf(ImdbIdRequired.class);
    }

    @Test
    void givenRatingBelow1_thenIncorrectRatingThrown() {
        assertThatThrownBy(() -> rateFilmUseCase.rate("token", "id", 0))
                .isInstanceOf(IncorrectRating.class);
    }

    @Test
    void givenRatingGreater10_thenIncorrectRatingThrown() {
        assertThatThrownBy(() -> rateFilmUseCase.rate("token", "id", 11))
                .isInstanceOf(IncorrectRating.class);
    }

    @Test
    void givenUserRateNonexistentFilm_thenFilmNotFoundThrown() {
        assertThatThrownBy(() -> rateFilmUseCase.rate("token", "id", 1))
                .isInstanceOf(FilmNotFound.class);
    }

    @Test
    void givenUserRatedFilm_thenFilmHasExactRatingInSearchResult() {
        rateFilmUseCase.rate("token", "id1", 10);

        assertThat(searchFilmUseCase.search("token", "Rated1", 0))
                .isEqualTo(
                        new PageStub(
                                new FilmDtoStub("id1", "Rated1", false, 10, 4)
                        )
                );
    }

    @Test
    void givenApiTokenINull_whenListTopRatedFilms_thenApiTokenRequiredThrown() {
        assertThatThrownBy(() ->
                listTopRatedFilmsUseCase.topRatedSortedByBoxOffice(null, 0, 0)
        )
                .isInstanceOf(ApiTokenRequired.class);
    }

    @Test
    void givenApiTokenIsEmpty_whenListTopRatedFilms_thenApiTokenRequiredThrown() {
        assertThatThrownBy(() ->
                listTopRatedFilmsUseCase.topRatedSortedByBoxOffice("", 0, 0)
        )
                .isInstanceOf(ApiTokenRequired.class);
    }

    @Test
    void givenPageNumberIsLessThanZero_whenListTopRatedFilms_thenPageNumberLessThanZeroThrown() {
        assertThatThrownBy(() ->
                listTopRatedFilmsUseCase.topRatedSortedByBoxOffice("token", -1, 0)
        )
                .isInstanceOf(PageNumberLessThanZero.class);
    }

    @Test
    void givenElementsOnPageIsLessThanZero_whenListTopRatedFilms_thenElementsOnPageLessThanZeroThrown() {
        assertThatThrownBy(() ->
                listTopRatedFilmsUseCase.topRatedSortedByBoxOffice("token", 0, -1)
        )
                .isInstanceOf(ElementsOnPageLessThanZero.class);
    }
}
