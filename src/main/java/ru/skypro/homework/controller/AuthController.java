package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.exception.BadRequestException;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.service.AuthService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public void login(@RequestBody Login login) {
        if (!authService.login(login.getUsername(), login.getPassword())) {
            throw new UnauthorizedException("Неверные учетные данные");
        }
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody Register register) {
        if (!authService.register(register)) {
            throw new BadRequestException("Ошибка регистрации пользователя");
        }
    }
}
