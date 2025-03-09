package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "DTO для создания или обновления объявления")
public class CreateOrUpdateAdDTO {

    @Schema(description = "Заголовок объявления", example = "Продам велосипед")
    private String title;

    @Schema(description = "Цена объявления", example = "15000")
    private int price;

    @Schema(description = "Описание объявления", example = "В хорошем состоянии, использовался один сезон")
    private String description;

    @Schema(description = "Автор объявления (заполняется автоматически на основе аутентификации)", hidden = true)
    private String author;

    // Явно добавляем конструктор со всеми полями:
    public CreateOrUpdateAdDTO(String title, int price, String description, String author) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.author = author;
    }
}
