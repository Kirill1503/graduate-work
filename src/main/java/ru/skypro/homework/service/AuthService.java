package ru.skypro.homework.service;

import ru.skypro.homework.dto.Register;

/**
 * Сервис для аутентификации и регистрации пользователей.
 */
public interface AuthService {
    /**
     * Выполняет вход пользователя.
     * @param userName имя пользователя
     * @param password пароль
     * @return true, если вход успешен, иначе false
     */
    boolean login(String userName, String password);

    /**
     * Регистрирует нового пользователя.
     * @param register данные для регистрации
     * @return true, если регистрация успешна, иначе false
     */
    boolean register(Register register);
}