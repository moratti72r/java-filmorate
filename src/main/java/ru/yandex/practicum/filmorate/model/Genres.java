package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.util.Objects;


@Getter
@Setter
@AllArgsConstructor
public class Genres implements Comparable<Genres> {

    private int id;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genres genres = (Genres) o;
        return id == genres.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Genres g) {
        return this.getId() - g.getId();
    }
}

