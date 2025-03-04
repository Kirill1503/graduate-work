package ru.skypro.homework.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.GetUserDTO;
import ru.skypro.homework.dto.UpdateUserDTO;
import ru.skypro.homework.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/set_password")
    public void updateUserPassword(@RequestParam("oldPassword") String oldPassword,
                                   @RequestParam("newPassword") String newPassword) {
        userService.updateUserPassword(oldPassword, newPassword);
    }

    @GetMapping("/me")
    public GetUserDTO getUserInformation() {
        return userService.getUserInformation();
    }

    @PatchMapping("/me")
    public UpdateUserDTO updateUserInformation(@RequestBody UpdateUserDTO updateUserDTO) {
        return userService.updateUserInformation(updateUserDTO);
    }

    @PatchMapping("/me/image")
    public void updateUserAvatar(@RequestParam("file") MultipartFile file) {
        userService.updateUserAvatar(file);
    }
}
