package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO для получения подробной информации об объявлении")
public class AdDTOForGet {
    @Schema(description = "Первичный ключ объявления", example = "1")
    private Long pk;

    @Schema(description = "Имя автора объявления", example = "Иван")
    private String authorFirstName;

    @Schema(description = "Фамилия автора объявления", example = "Иванов")
    private String authorLastName;

    @Schema(description = "Описание объявления", example = "Продается новый велосипед")
    private String description;

    @Schema(description = "Email автора объявления", example = "ivan@example.com")
    private String email;

    @Schema(description = "Имя файла с изображением объявления", example = "16161616_image.png")
    private String image;

    @Schema(description = "Телефон автора объявления", example = "+7 (123) 456-78-90")
    private String phone;

    @Schema(description = "Цена объявления", example = "15000")
    private int price;

    @Schema(description = "Заголовок объявления", example = "Продам велосипед")
    private String title;
}
