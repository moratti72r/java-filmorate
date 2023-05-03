package ru.yandex.practicum.filmorate.exception;

public class EntityNotFoundException extends RuntimeException {
    private Class clazz;

    public EntityNotFoundException(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getMessage() {
        return clazz.getName();
    }
}