package ru.skypro.homework.utils;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.model.Comment;

@Component
public class MappingCommentDTO {

    public static CommentDTO mapToDTO(Comment comment) {
        return new CommentDTO(
                comment.getId(),
                comment.getAuthor().getImage(),
                comment.getAuthor().getFirstName(),
                comment.getCreatedAt(),
                comment.getId(),
                comment.getText()
        );
    }
}
