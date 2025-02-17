package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.GetUserDTO;
import ru.skypro.homework.dto.UpdateUserDTO;

public interface UserService {
    void updateUserPassword(String oldPassword, String newPassword);

    GetUserDTO getUserInformation();

    UpdateUserDTO updateUserInformation(UpdateUserDTO updateUserDTO);

    void updateUserAvatar(MultipartFile avatar);
}