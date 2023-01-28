package ru.yandex.practicum.filmorate.validationtests;

import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilmTest {
    private Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void validationErrorWithNameIsEmpty() {

        Film film = new Film();
        film.setName("");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(1991, 12, 15));
        film.setDuration(100);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.size() == 1);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("не должно быть пустым")));
    }

    @Test
    public void validationErrorWithDescriptionExceeds200Characters() {

        Film film = new Film();
        film.setName("film");
        film.setDescription("description,description,description,description,description,description,description," +
                "description,description,description,description,description,description,description,description," +
                "description,description,description,description");
        film.setReleaseDate(LocalDate.of(1991, 12, 15));
        film.setDuration(100);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.size() == 1);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("размер должен находиться в диапазоне от 0 до 200")));
    }

    @Test
    public void validationErrorWithReleaseDateUnsuitableByCondition() {

        Film film = new Film();
        film.setName("film");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(1894, 12, 15));
        film.setDuration(100);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.size() == 1);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("{TimeAfterDate.invalid}")));
    }

    @Test
    public void validationErrorWithDurationIsNegative() {

        Film film = new Film();
        film.setName("film");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(1991, 12, 15));
        film.setDuration(-100);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.size() == 1);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("должно быть не меньше 1")));
    }

    @Test
    public void creatingFilmWithAllNonValidVariables() {

        Film film = new Film();
        film.setName("");
        film.setDescription("description,description,description,description,description,description,description," +
                "description,description,description,description,description,description,description,description," +
                "description,description,description,description");
        film.setReleaseDate(LocalDate.of(1894, 12, 15));
        film.setDuration(-100);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.size() == 4);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("должно быть не меньше 1")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("{TimeAfterDate.invalid}")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("размер должен находиться в диапазоне от 0 до 200")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("не должно быть пустым")));
    }


    @Test
    public void creatingFilmWithoutValidationErrors() {

        Film film = new Film();
        film.setName("film");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(1991, 12, 15));
        film.setDuration(100);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.isEmpty());
        assertEquals("film", film.getName());
        assertEquals("description", film.getDescription());
        assertEquals(LocalDate.of(1991, 12, 15), film.getReleaseDate());
        assertTrue(100 == film.getDuration());
    }
}
