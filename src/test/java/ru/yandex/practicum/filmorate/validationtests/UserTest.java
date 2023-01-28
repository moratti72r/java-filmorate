package ru.yandex.practicum.filmorate.validationtests;

import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void validationErrorWithIncorrectEmailFormat() {

        User user = new User();
        user.setEmail("user.com");
        user.setName("User");
        user.setLogin("UserLogin");
        user.setBirthday(LocalDate.of(1991, 12, 15));
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.size() == 1);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("должно иметь формат адреса электронной почты")));
    }

    @Test
    public void validationErrorWithLoginHavingSpaces() {

        User user = new User();
        user.setEmail("user@mail.com");
        user.setName("User");
        user.setLogin("  ");
        user.setBirthday(LocalDate.of(1991, 12, 15));
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.size() == 1);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("не должно быть пустым")));
    }

    @Test
    public void validationErrorWithBirthdayUnsuitableByCondition() {

        User user = new User();
        user.setEmail("user@mail.com");
        user.setName("User");
        user.setLogin("UserLogin");
        user.setBirthday(LocalDate.of(2024, 12, 15));
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.size() == 1);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("должно содержать прошедшую дату")));
    }

    @Test
    public void creatingUserWithAllNonValidVariables() {
        User user = new User();
        user.setEmail("usermail.com");
        user.setName("User");
        user.setLogin("  ");
        user.setBirthday(LocalDate.of(2024, 12, 15));
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.size() == 3);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("должно содержать прошедшую дату")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("не должно быть пустым")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("должно иметь формат адреса электронной почты")));
    }

    @Test
    public void creatingUserWithoutValidationErrors() {

        User user = new User();
        user.setEmail("user@mail.com");
        user.setName("User");
        user.setLogin("UserLogin");
        user.setBirthday(LocalDate.of(1991, 12, 15));
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty());
        assertEquals("user@mail.com", user.getEmail());
        assertEquals("User", user.getName());
        assertEquals("UserLogin", user.getLogin());
        assertEquals(LocalDate.of(1991, 12, 15), user.getBirthday());
    }
}
