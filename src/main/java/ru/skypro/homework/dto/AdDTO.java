package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skypro.homework.model.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdDTO {
    private User author;
    private String image;
    private Long pk;
    private Integer price;
    private String title;
}