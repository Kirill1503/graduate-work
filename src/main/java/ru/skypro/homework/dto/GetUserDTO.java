package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class GetUserDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String image;
    private Role role;
}
