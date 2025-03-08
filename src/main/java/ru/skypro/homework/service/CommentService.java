package ru.skypro.homework.service;

import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CommentResponseDTO;
import ru.skypro.homework.dto.CreateOrUpdateCommentDTO;

/**
 * Сервис для управления комментариями.
 */
public interface CommentService {
    /**
     * Получает комментарии к объявлению.
     * @param adId идентификатор объявления
     * @return DTO со списком комментариев
     */
    CommentResponseDTO getComments(Long adId);

    /**
     * Добавляет комментарий к объявлению.
     * @param adId идентификатор объявления
     * @param dto данные для создания комментария
     * @return DTO созданного комментария
     */
    CommentDTO addComment(Long adId, CreateOrUpdateCommentDTO dto);

    /**
     * Удаляет комментарий.
     * @param adId идентификатор объявления
     * @param commentId идентификатор комментария
     */
    void deleteComment(Long adId, Long commentId);

    /**
     * Обновляет комментарий.
     * @param adId идентификатор объявления
     * @param commentId идентификатор комментария
     * @param dto данные для обновления
     * @return обновленное DTO комментария
     */
    CommentDTO updateComment(Long adId, Long commentId, CreateOrUpdateCommentDTO dto);
}