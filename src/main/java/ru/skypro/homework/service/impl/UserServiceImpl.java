package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.GetUserDTO;
import ru.skypro.homework.dto.UpdateUserDTO;
import ru.skypro.homework.exception.ThePasswordIsNotTrue;
import ru.skypro.homework.exception.TheUserIsNotAuthenticated;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.FileStorageService;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.utils.MappingUserDTO;
import ru.skypro.homework.utils.SecurityUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String AVATAR_DIR = "uploads/avatars/";

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final FileStorageService fileStorageService;

    @Override
    public void updateUserPassword(String oldPassword, String newPassword) {
        User user = getAuthenticatedUser();
        if (!encoder.matches(oldPassword, user.getPassword())) {
            throw new ThePasswordIsNotTrue("The old password is incorrect");
        }
        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public GetUserDTO getUserInformation() {
        User user = getAuthenticatedUser();
        return MappingUserDTO.mapToUserDTOForGetUserInformation(user);
    }

    @Override
    public UpdateUserDTO updateUserInformation(UpdateUserDTO updateUserDTO) {
        User user = getAuthenticatedUser();
        user.setFirstName(updateUserDTO.getFirstName());
        user.setLastName(updateUserDTO.getLastName());
        user.setPhone(updateUserDTO.getPhone());
        userRepository.save(user);
        return MappingUserDTO.mapToUserDTOForUpdateUser(user);
    }

    @Override
    public void updateUserAvatar(MultipartFile avatar) {
        User user = getAuthenticatedUser();
        if (avatar.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty");
        }
        try {
            File uploadDir = new File(AVATAR_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            String fileName = "avatar_" + user.getId() + "_" + System.currentTimeMillis() + ".png";
            Path filePath = Paths.get(AVATAR_DIR + fileName);
            Files.write(filePath, avatar.getBytes());
            user.setImage(fileName);
            userRepository.save(user);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save avatar", e);
        }
    }

    private User getAuthenticatedUser() {
        return Optional.ofNullable(SecurityUtils.getCurrentUsername())
                .map(userRepository::findUserByUsername)
                .orElseThrow(() -> new TheUserIsNotAuthenticated("The user is not authenticated"));
    }
}