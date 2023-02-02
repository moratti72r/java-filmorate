package ru.yandex.practicum.filmorate.exception;

public class IncorrectArgumentsException extends RuntimeException {

    String message;

    public IncorrectArgumentsException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
