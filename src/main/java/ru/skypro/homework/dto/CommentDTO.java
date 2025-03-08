package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO для получения информации о комментарии")
public class CommentDTO {
    @Schema(description = "ID автора комментария", example = "1")
    private long pk;

    @Schema(description = "Ссылка на аватар автора комментария", example = "avatar1.png")
    private String authorImage;

    @Schema(description = "Имя автора комментария", example = "Иван")
    private String authorFirstName;

    @Schema(description = "Дата и время создания комментария", example = "2023-03-08T10:15:30Z")
    private Instant createdAt;

    @Schema(description = "ID комментария", example = "100")
    private long commentId;

    @Schema(description = "Текст комментария", example = "Отличное объявление!")
    private String text;
}
