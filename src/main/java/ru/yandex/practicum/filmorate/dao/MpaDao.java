package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

public interface MpaDao {

    List<MPA> getAll ();
    MPA getById (Integer id);
}
