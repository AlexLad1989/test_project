package com.github.xini1.exception;

/**
 * @author Alexandr Ladoshkin
 */
public final class IncorrectRating extends RuntimeException {

    public IncorrectRating() {
        super("Rating can not be less than 1 and greater than 10");
    }
}
