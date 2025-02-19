package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CommentResponseDTO;
import ru.skypro.homework.dto.CreateOrUpdateCommentDTO;
import ru.skypro.homework.service.CommentService;

@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class CommentsController {

    private final CommentService commentService;

    @GetMapping("{adId}/comments")
    public CommentResponseDTO getComments(@PathVariable Long adId) {
        return commentService.getComments(adId);
    }

    @PostMapping("{adId}/comments")
    public CommentDTO addComment(@PathVariable Long adId,
                                 @RequestBody CreateOrUpdateCommentDTO dto) {
        return commentService.addComment(adId, dto);
    }

    @DeleteMapping("{adId}/comments/{commentId}")
    public void deleteComment(@PathVariable Long adId,
                              @PathVariable Long commentId) {
        commentService.deleteComment(adId, commentId);
    }

    @PatchMapping("{adId}/comments/{commentId}")
    public CommentDTO updateComment(@PathVariable Long adId,
                                    @PathVariable Long commentId,
                                    @RequestBody CreateOrUpdateCommentDTO dto) {
        return commentService.updateComment(adId, commentId, dto);
    }
}