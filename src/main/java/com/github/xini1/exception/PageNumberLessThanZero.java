package com.github.xini1.exception;

/**
 * @author Alexandr Ladoshkin
 */
public final class PageNumberLessThanZero extends RuntimeException {

    public PageNumberLessThanZero() {
        super("Page number can not be less than 0");
    }
}
