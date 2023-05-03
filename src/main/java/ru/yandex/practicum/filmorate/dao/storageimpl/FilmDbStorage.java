package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.constraints.Min;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.*;

@Slf4j
@Component
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final String sq = "SELECT fgm.id,fgm.name,fgm.description,fgm.release_date,fgm.duration,fgm.likes," +
            "fgm.mpa,m.name_mpa,fgm.id_genre,fgm.name_genre " +
            "FROM mpa AS m RIGHT JOIN (SELECT * FROM films AS f LEFT JOIN " +
            "(SELECT fg.id_film,fg.id_genre,g.name_genre " +
            "FROM films_genres AS fg LEFT JOIN genres AS g " +
            "ON g.id=fg.id_genre) " +
            "ON f.id=id_film) AS fgm ON m.id=mpa ";

    @Override
    public List<Film> getAll() {
        log.info("Список фильмов получен");
        return new ArrayList<>(jdbcTemplate.query(sq, extractor()).values());
    }

    @Override
    public Film create(Film film) {

        String sqlQuery = "INSERT INTO films (name," +
                "description," +
                "release_date," +
                "duration," +
                "likes," +
                "mpa) " +
                "VALUES (?,?,?,?,?,?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement psst = connection.prepareStatement(sqlQuery, new String[]{"id"});
            psst.setString(1, film.getName());
            psst.setString(2, film.getDescription());
            psst.setDate(3, Date.valueOf(film.getReleaseDate()));
            psst.setInt(4, film.getDuration());
            psst.setInt(5, film.getLikes());
            psst.setInt(6, film.getMpa().getId());
            return psst;
        }, keyHolder);

        film.setId(keyHolder.getKey().intValue());

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
        SqlRowSet filmsRows = jdbcTemplate.queryForRowSet("select * from films where id = ?", film.getId());
        if (filmsRows.next()) {
            String sqlQuery = "UPDATE films SET name = ?," +
                    "description = ?," +
                    "release_date = ?," +
                    "duration = ?," +
                    "likes = ?," +
                    "mpa = ?" +
                    " WHERE id = ?";

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
//            log.warn("Фильм с id " + film.getId() + " отсутствует");
            throw new EntityNotFoundException(FilmDbStorage.class);
        }
        log.info("Фильм с id=" + film.getId() + " успешно изменен");
        return film;
    }

    @Override
    public boolean contains(Integer id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select 1 from films where id = ?", id);
        return filmRows.next();
    }

    @Override
    public List<Film> getTopFilms(int limit) {
        String sqlQuery = sq + "ORDER BY likes DESC LIMIT ?";
        log.info("Получены " + limit + " наиболее популярных фильмов");
        return new ArrayList<>(jdbcTemplate.query(sqlQuery, extractor(), limit).values());
    }

    @Override
    public Film getById(Integer id) {
        if (contains(id)) {
            String sqq = "SELECT fgm.id,fgm.name,fgm.description,fgm.release_date,fgm.duration,fgm.likes," +
                    "fgm.mpa,m.name_mpa,fgm.id_genre,fgm.name_genre " +
                    "FROM mpa AS m RIGHT JOIN (SELECT * FROM films AS f LEFT JOIN " +
                    "(SELECT fg.id_film,fg.id_genre,g.name_genre " +
                    "FROM films_genres AS fg LEFT JOIN genres AS g " +
                    "ON g.id=fg.id_genre ) " +
                    "ON f.id=id_film where f.id = ?) AS fgm ON m.id=mpa ";
            Film film = jdbcTemplate.query(sqq, extractor(), id).get(id);
            log.info("Фильм с id=" + id + " получен");
            return film;
        } else {
//            log.warn("Фильм с id " + id + " отсутствует");
            throw new EntityNotFoundException(FilmDbStorage.class);
        }
    }

    private ResultSetExtractor<Map<Integer, Film>> extractor() {
        return rs -> {

            Map<Integer, Film> films = new HashMap<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                int idGenre = rs.getInt("id_genre");
                String nameGenre = rs.getString("name_genre");
                if (films.containsKey(id)) {
                    if (idGenre != 0) films.get(id).getGenres().add(new Genres(idGenre, nameGenre));
                } else {
                    Film film = new Film();
                    films.put(id, film);
                    film.setId(id);
                    film.setName(rs.getString("name"));
                    film.setDescription(rs.getString("description"));
                    film.setReleaseDate(rs.getDate("release_date").toLocalDate());
                    film.setDuration(rs.getInt("duration"));
                    film.setLikes(rs.getInt("likes"));

                    if (idGenre != 0) films.get(id).getGenres().add(new Genres(idGenre, nameGenre));

                    int idMpa = rs.getInt("mpa");
                    String nameMpa = rs.getString("name_mpa");

                    film.setMpa(new MPA(idMpa, nameMpa));

                }
            }
            return films;
        };
    }


}
