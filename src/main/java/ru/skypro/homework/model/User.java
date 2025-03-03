package ru.skypro.homework.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skypro.homework.dto.Role;
import javax.validation.constraints.Pattern;
import javax.persistence.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String username;
    private String firstName;
    private String lastName;

    @Column(nullable = false)
    @Pattern(regexp = "\\+7\\s?\\(?!\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    private String phone;
    private String image;

    @Enumerated(EnumType.STRING)
    private Role role;
}