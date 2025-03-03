package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private Long pk;
    private String authorImage;
    private String authorFirstName;
    private Instant createdAt;
    private Long commentId;
    private String text;
}