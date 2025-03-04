package ru.skypro.homework.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateOrUpdateAdDTO {
    private String title;
    private int price;
    private String description;
    private String author;

}
