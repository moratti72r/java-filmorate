package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exception.MPANotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Slf4j
@Component
@Primary
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<Film> filmRowMapper() {
        return (rs, rowNum) -> {
            Film film = new Film();

            film.setId(rs.getInt("id_film"));
            film.setName(rs.getString("name_film"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getDate("release_date").toLocalDate());
            film.setDuration(rs.getInt("duration"));
            film.setLikes(rs.getInt("likes"));

            String sq = "SELECT * FROM genres WHERE id_genre IN " +
                    "(SELECT id_genre FROM films_genres " +
                    "WHERE id_film=?)";
            film.setGenres(new HashSet<>(jdbcTemplate.query(sq, genreRowMapper(), film.getId())));

            Integer idMpa = rs.getInt("mpa");
            film.setMpa(getMpaById(idMpa));

            return film;
        };
    }

    private RowMapper<Genres> genreRowMapper() {
        return (rs, rowNum) -> {
            Genres genre = new Genres();
            genre.setId(rs.getInt("id_genre"));
            genre.setName(rs.getString("name_genre"));
            return genre;
        };
    }

    private RowMapper<MPA> mpaRowMapper() {
        return (rs, rowNum) -> {
            MPA mpa = new MPA();
            mpa.setId(rs.getInt("id_mpa"));
            mpa.setName(rs.getString("name_mpa"));
            return mpa;
        };
    }

    @Override
    public List<Film> getAll() {
        String sqlQuery = "SELECT * FROM films";
        log.info("Список фильмов получен");
        return jdbcTemplate.query(sqlQuery, filmRowMapper());
    }

    @Override
    public Film create(Film film) {

        String sqlQuery = "INSERT INTO films (name_film," +
                "description," +
                "release_date," +
                "duration," +
                "likes," +
                "mpa) " +
                "VALUES (?,?,?,?,?,?)";

        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getLikes(),
                film.getMpa().getId());

        int idFilm = jdbcTemplate.queryForObject("SELECT id_film FROM films WHERE name_film = ?", Integer.class, film.getName());
        film.setId(idFilm);

        if (!film.getGenres().isEmpty()) {
            for (Genres gen : film.getGenres()) {
                String sQuery = "INSERT INTO films_genres (id_film,id_genre) " +
                        "VALUES (?,?)";

                jdbcTemplate.update(sQuery,
                        film.getId(),
                        gen.getId());
            }
        }
        log.info("Фильм с id=" + film.getId() + " успешно добавлен");
        return film;
    }

    @Override
    public Film upDate(Film film) {
        SqlRowSet filmsRows = jdbcTemplate.queryForRowSet("select * from films where id_film = ?", film.getId());
        if (filmsRows.next()) {
            String sqlQuery = "UPDATE films SET name_film = ?," +
                    "description = ?," +
                    "release_date = ?," +
                    "duration = ?," +
                    "likes = ?," +
                    "mpa = ?" +
                    " WHERE id_film = ?";

            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getLikes(),
                    film.getMpa().getId(),
                    film.getId());

            jdbcTemplate.update("DELETE FROM films_genres WHERE id_film = ?", film.getId());

            if (!film.getGenres().isEmpty()) {
                for (Genres gen : film.getGenres()) {
                    String sQuery = "INSERT INTO films_genres (id_film,id_genre) " +
                            "VALUES (?,?)";

                    jdbcTemplate.update(sQuery,
                            film.getId(),
                            gen.getId());
                }
            }
        } else {
            log.warn("Фильм с id " + film.getId() + " отсутствует");
            throw new FilmNotFoundException("Фильм с id " + film.getId() + " отсутствует");
        }
        log.info("Фильм с id=" + film.getId() + " успешно изменен");
        return film;
    }

    @Override
    public boolean contains(Integer id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from films where id_film = ?", id);
        return filmRows.next();
    }

    @Override
    public List<Film> getTopFilms(int limit) {
        String sqlQuery = "SELECT * FROM films ORDER BY likes DESC LIMIT ?";
        log.info("Получены " + limit + " наиболее популярных фильмов");
        return jdbcTemplate.query(sqlQuery, filmRowMapper(), limit);
    }

    @Override
    public Film getById(Integer id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from films where id_film = ?", id);
        if (filmRows.next()) {

            Film film = new Film();

            film.setId(id);
            film.setName(filmRows.getString("name_film"));
            film.setDescription(filmRows.getString("description"));
            film.setReleaseDate(filmRows.getDate("release_date").toLocalDate());
            film.setDuration(filmRows.getInt("duration"));
            film.setLikes(filmRows.getInt("likes"));

            String sq = "SELECT * FROM genres WHERE id_genre IN " +
                    "(SELECT id_genre FROM films_genres WHERE id_film=?)";
            film.setGenres(new HashSet<Genres>(jdbcTemplate.query(sq, genreRowMapper(), film.getId())));

            Integer idMpa = filmRows.getInt("mpa");
            film.setMpa(getMpaById(idMpa));

            log.info("Фильм с id=" + id + " получен");
            return film;
        } else {
            log.warn("Фильм с id " + id + " отсутствует");
            throw new FilmNotFoundException("Фильм с id " + id + " отсутствует");
        }
    }

    @Override
    public List<Genres> getAllGenres() {
        String sq = "SELECT * FROM genres";
        log.info("Список жанров получен");
        return jdbcTemplate.query(sq, genreRowMapper());
    }

    @Override
    public Genres getGenreById(Integer id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from genres where id_genre = ?", id);
        if (filmRows.next()) {
            String sq = "SELECT * FROM genres WHERE id_genre = ?";
            log.info("Жанр с id=" + id + " получен");
            return jdbcTemplate.queryForObject(sq, genreRowMapper(), id);
        } else {
            log.warn("Жанр с id=" + id + " не найден");
            throw new GenreNotFoundException("Жанр с id=" + id + " не найден");
        }
    }

    @Override
    public List<MPA> getAllMPA() {
        String sq = "SELECT * FROM mpa";
        log.info("Список возрастных рейтингов получен");
        return jdbcTemplate.query(sq, mpaRowMapper());
    }

    @Override
    public MPA getMpaById(Integer id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from mpa where id_mpa = ?", id);
        if (filmRows.next()) {
            String sq = "SELECT * FROM mpa WHERE id_mpa = ?";
            log.info("Возрастной рейтинг с id=" + id + " получен");
            return jdbcTemplate.queryForObject(sq, mpaRowMapper(), id);
        } else {
            log.warn("Возрастное ограничение с id=" + id + " не найдено");
            throw new MPANotFoundException("Возрастное ограничение с id=" + id + " не найдено");
        }
    }
}
