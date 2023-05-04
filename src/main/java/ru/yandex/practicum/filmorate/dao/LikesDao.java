package ru.yandex.practicum.filmorate.dao;

public interface LikesDao {
    int addLike(int idFilm, int idUser);

    int removeLike(int idFilm, int idUser);

}
