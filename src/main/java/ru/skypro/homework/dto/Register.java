package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skypro.homework.dto.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO для регистрации нового пользователя")
public class Register {

    @NotBlank
    @Schema(description = "Имя пользователя", example = "Иван")
    private String firstName;

    @NotBlank
    @Schema(description = "Фамилия пользователя", example = "Иванов")
    private String lastName;

    @Email
    @NotBlank
    @Schema(description = "Email пользователя", example = "user@gmail.com")
    private String email;

    @Size(min = 6, max = 30)
    @Schema(description = "Пароль пользователя", example = "securepassword123")
    private String password;

    @Schema(description = "Роль пользователя", example = "USER")
    private Role role;
}