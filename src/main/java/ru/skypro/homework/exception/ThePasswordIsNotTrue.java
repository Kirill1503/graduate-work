package ru.skypro.homework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ThePasswordIsNotTrue extends RuntimeException {
    public ThePasswordIsNotTrue(String message) {
        super(message);
    }
}
