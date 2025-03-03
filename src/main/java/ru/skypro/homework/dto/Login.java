package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Модель логина пользователя")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Login {

    @Schema(description = "Логин пользователя", example = "user@gmail.com", minLength = 4, maxLength = 32)
    private String username;

    @Schema(description = "Пароль пользователя", example = "password", minLength = 8, maxLength = 16)
    private String password;
}
