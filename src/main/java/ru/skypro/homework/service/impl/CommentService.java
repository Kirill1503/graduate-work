package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CreateOrUpdateCommentDTO;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.repository.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getCommentsByAdId(Integer adId) {
        return commentRepository.findByAdId(adId);
    }

    public Optional<Comment> getCommentById(Integer id) {
        return commentRepository.findById(id);
    }

    public Comment createComment(CreateOrUpdateCommentDTO dto, Long adId, Long userId) {
        Comment comment = new Comment();
        comment.setText(dto.getText());
        comment.setAdId(comment.getAd().getId());
        comment.setAuthorId(comment.getAuthor().getId());
        return commentRepository.save(comment);
    }

    public void deleteComment(Integer id) {
        commentRepository.deleteById(id);
    }

    public Optional<Comment> updateComment(Integer id, CreateOrUpdateCommentDTO dto) {
        return commentRepository.findById(id).map(comment -> {
            comment.setText(dto.getText());
            return commentRepository.save(comment);
        });
    }
}