package ru.practicum.shareit.exception;

public class WrongArgumentsException extends RuntimeException {
    public WrongArgumentsException(String message) {
        super(message);
    }
}
