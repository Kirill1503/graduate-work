package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO для обновления профиля пользователя")
public class UpdateUserDTO {

    @NotBlank
    @Schema(description = "Имя пользователя", example = "Иван")
    private String firstName;

    @NotBlank
    @Schema(description = "Фамилия пользователя", example = "Иванов")
    private String lastName;

    @Email
    @Schema(description = "Email пользователя (не может быть изменен)", example = "user@gmail.com")
    private String email;

    @Schema(description = "Телефон пользователя", example = "+7 (999) 123-45-67")
    private String phone;
}
