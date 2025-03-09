package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO для аутентификации пользователя")
public class Login {

    @NotBlank
    @Schema(description = "Имя пользователя (email)", example = "user@gmail.com")
    private String username;

    @NotBlank
    @Schema(description = "Пароль пользователя", example = "securepassword123")
    private String password;
}
