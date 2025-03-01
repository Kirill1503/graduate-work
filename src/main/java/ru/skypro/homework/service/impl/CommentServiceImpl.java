package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CommentResponseDTO;
import ru.skypro.homework.dto.CreateOrUpdateCommentDTO;
import ru.skypro.homework.exception.AdNotFound;
import ru.skypro.homework.exception.CommentNotFound;
import ru.skypro.homework.exception.TheUserIsNotAuthenticated;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.utils.MappingCommentDTO;
import ru.skypro.homework.utils.SecurityUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;

    @Override
    public CommentResponseDTO getComments(Long adId) {
        List<CommentDTO> comments = commentRepository.findByAd_Id(adId)
                .stream()
                .map(MappingCommentDTO::mapToDTO)
                .collect(Collectors.toList());

        return new CommentResponseDTO(comments.size(), comments);
    }

    @Override
    public CommentDTO addComment(Long adId, CreateOrUpdateCommentDTO dto) {
        Ad ad = adRepository.findById(adId).orElseThrow(() -> new AdNotFound("Объявление не найдено"));
        User user = getAuthenticatedUser();

        Comment comment = new Comment();
        comment.setAd(ad);
        comment.setAuthor(user);
        comment.setText(dto.getText());

        Comment savedComment = commentRepository.save(comment);
        return MappingCommentDTO.mapToDTO(savedComment);
    }

    @Override
    public void deleteComment(Long adId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFound("Comment not found"));
        commentRepository.delete(comment);
    }

    @Override
    public CommentDTO updateComment(Long adId, Long commentId, CreateOrUpdateCommentDTO dto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFound("Комментарий не найден"));

        comment.setText(dto.getText());
        Comment updatedComment = commentRepository.save(comment);
        return MappingCommentDTO.mapToDTO(updatedComment);
    }

    private User getAuthenticatedUser() {
        return Optional.ofNullable(securityUtils.getCurrentUsername())
                .map(userRepository::findUserByUsername)
                .orElseThrow(() -> new TheUserIsNotAuthenticated("The user is not authenticated"));
    }
}