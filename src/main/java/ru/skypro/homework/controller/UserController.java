package ru.skypro.homework.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.GetUserDTO;
import ru.skypro.homework.dto.UpdateUserDTO;
import ru.skypro.homework.service.impl.UserServiceImpl;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServiceImpl userServiceImpl;

    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/set_password")
    public void updateUserPassword(@RequestParam("oldPassword") String oldPassword,
                                   @RequestParam("newPassword") String newPassword) {
        userServiceImpl.updateUserPassword(oldPassword, newPassword);
    }

    @GetMapping("/me")
    public GetUserDTO getUserInformation() {
        return userServiceImpl.getUserInformation();
    }

    @PatchMapping("/me")
    public UpdateUserDTO updateUserInformation(@RequestBody UpdateUserDTO updateUserDTO) {
        return userServiceImpl.updateUserInformation(updateUserDTO);
    }

    @PatchMapping("/me/image")
    public void updateUserAvatar(@RequestParam("file") MultipartFile file) {
        userServiceImpl.updateUserAvatar(file);
    }
}
