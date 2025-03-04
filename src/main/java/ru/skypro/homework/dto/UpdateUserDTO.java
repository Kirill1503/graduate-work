package ru.skypro.homework.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateUserDTO {
    private String firstName;
    private String lastName;
    private String phone;
    private String username;

}
