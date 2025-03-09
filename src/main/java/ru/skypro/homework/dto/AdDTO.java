package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO для получения краткой информации об объявлении")
public class AdDTO {
    @Schema(description = "ID автора объявления", example = "1")
    private Long author;

    @Schema(description = "Имя картинки объявления", example = "1234567890_image.png")
    private String image;

    @Schema(description = "Первичный ключ объявления", example = "1")
    private Long pk;

    @Schema(description = "Цена объявления", example = "15000")
    private Integer price;

    @Schema(description = "Заголовок объявления", example = "Продам велосипед")
    private String title;
}