package ru.skypro.homework.service;

import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CommentResponseDTO;
import ru.skypro.homework.dto.CreateOrUpdateCommentDTO;


public interface CommentService {

    CommentResponseDTO getComments(Long adId);

    CommentDTO addComment(Long adId, CreateOrUpdateCommentDTO dto);

    void deleteComment(Long adId, Long commentId);

    CommentDTO updateComment(Long adId, Long commentId, CreateOrUpdateCommentDTO dto);
}
