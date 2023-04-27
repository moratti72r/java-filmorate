package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genres;

import java.util.Map;

public class InMemoryGenreStorage {
    private static final Map<Integer, Genres> genres = Map.of(1, new Genres(1, "Комедия"),
            2, new Genres(2, "Драма"),
            3, new Genres(3, "Мультфильм"),
            4, new Genres(4, "Триллер"),
            5, new Genres(5, "Документальный"),
            6, new Genres(6, "Боевик"));

    public static Map<Integer, Genres> getGenres() {
        return genres;
    }
}
