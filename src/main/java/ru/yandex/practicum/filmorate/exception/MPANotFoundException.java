package ru.yandex.practicum.filmorate.exception;

public class MPANotFoundException extends RuntimeException {

    String message;

    public MPANotFoundException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
