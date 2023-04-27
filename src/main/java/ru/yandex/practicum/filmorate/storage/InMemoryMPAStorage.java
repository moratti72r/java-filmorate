package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.Map;

public class InMemoryMPAStorage {
    private static final Map<Integer, MPA> mpa = Map.of(1, new MPA(1, "G"),
            2, new MPA(2, "PG"),
            3, new MPA(3, "PG-13"),
            4, new MPA(4, "R"),
            5, new MPA(5, "NC-17"));

    public static Map<Integer, MPA> getMpa() {
        return mpa;
    }
}
