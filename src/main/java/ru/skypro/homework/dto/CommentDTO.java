package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private long pk;
    private String authorImage;
    private String authorFirstName;
    private Instant createdAt;
    private long commentId;
    private String text;
}