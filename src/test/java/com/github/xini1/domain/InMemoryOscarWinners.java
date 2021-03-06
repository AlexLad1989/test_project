package com.github.xini1.domain;

import com.github.xini1.port.OscarWinners;

import java.util.Collection;
import java.util.List;

/**
 * @author Alexandr Ladoshkin
 */
final class InMemoryOscarWinners implements OscarWinners {

    private final Collection<String> names;

    InMemoryOscarWinners(String... names) {
        this.names = List.of(names);
    }

    @Override
    public boolean isWinner(String name) {
        return names.contains(name);
    }
}
