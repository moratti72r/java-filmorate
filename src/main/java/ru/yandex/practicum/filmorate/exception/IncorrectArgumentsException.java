package ru.yandex.practicum.filmorate.exception;

public class IncorrectArgumentsException extends RuntimeException {

    private Class clazz;

    public IncorrectArgumentsException(Class clazz) {
        this.clazz = clazz;
    }

    public String getMessage() {
        return clazz.getName();
    }
}
