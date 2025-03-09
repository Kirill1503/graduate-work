package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.GetUserDTO;
import ru.skypro.homework.dto.UpdateUserDTO;

/**
 * Сервис для управления пользователями.
 */
public interface UserService {
    /**
     * Обновляет пароль пользователя.
     * @param oldPassword старый пароль
     * @param newPassword новый пароль
     */
    void updateUserPassword(String oldPassword, String newPassword);

    /**
     * Получает информацию о текущем пользователе.
     * @return DTO с информацией о пользователе
     */
    GetUserDTO getUserInformation();

    /**
     * Обновляет информацию о пользователе.
     * @param updateUserDTO данные для обновления
     * @return обновленное DTO пользователя
     */
    UpdateUserDTO updateUserInformation(UpdateUserDTO updateUserDTO);

    /**
     * Обновляет аватар пользователя.
     * @param avatar файл аватара
     */
    void updateUserAvatar(MultipartFile avatar);
}