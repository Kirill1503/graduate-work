package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO для смены пароля пользователя")
public class NewPasswordDTO {

    @NotBlank
    @Schema(description = "Текущий пароль", example = "oldpassword123")
    private String currentPassword;

    @Size(min = 6, max = 30)
    @Schema(description = "Новый пароль", example = "newsecurepassword456")
    private String newPassword;
}
