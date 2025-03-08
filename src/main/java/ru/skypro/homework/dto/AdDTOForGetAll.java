package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO для получения списка объявлений")
public class AdDTOForGetAll {
    @Schema(description = "Общее количество объявлений", example = "10")
    private int count;

    @Schema(description = "Список объявлений")
    private List<AdDTO> results;
}
