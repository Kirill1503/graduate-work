package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CommentResponseDTO;
import ru.skypro.homework.dto.CreateOrUpdateCommentDTO;
import ru.skypro.homework.exception.CommentNotFound;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.utils.MappingCommentDTO;
import ru.skypro.homework.utils.SecurityUtils;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private AdRepository adRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityUtils securityUtils;

    @Mock
    private MappingCommentDTO mappingCommentDTO;

    @InjectMocks
    private CommentServiceImpl commentService;

    private User user;
    private Ad ad;
    private Comment comment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        ad = new Ad();
        ad.setId(1L);

        comment = new Comment();
        comment.setId(1L);
        comment.setAuthor(user);
        comment.setAd(ad);
        comment.setText("testComment");
    }

    @Test
    void getComments_ShouldReturnComments() {
        when(commentRepository.findByAd_Id(ad.getId())).thenReturn(List.of(comment));
        when(mappingCommentDTO.mapToDTO(comment)).thenReturn(new CommentDTO(1L, "authorImage",
                "authorFirstName", Instant.MIN, 1L, "testComment"));

        CommentResponseDTO response = commentService.getComments(ad.getId());

        assertEquals(1, response.getCount());
        assertEquals("testComment", response.getResults().get(0).getText());
    }

    @Test
    void addComment_ShouldAddComment() {
        when(adRepository.findById(ad.getId())).thenReturn(Optional.of(ad));
        when(securityUtils.getCurrentUsername()).thenReturn(user.getUsername());
        when(userRepository.findUserByUsername(user.getUsername())).thenReturn(user);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(mappingCommentDTO.mapToDTO(comment)).thenReturn(new CommentDTO(1L, "authorImage",
                "authorFirstName", Instant.MIN, 1L, "testComment"));

        CreateOrUpdateCommentDTO dto = new CreateOrUpdateCommentDTO("testComment");
        CommentDTO result = commentService.addComment(ad.getId(), dto);

        assertEquals("testComment", result.getText());
    }

    @Test
    void deleteComment_ShouldDeleteComment() {
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        when(securityUtils.getCurrentUsername()).thenReturn(user.getUsername());
        when(userRepository.findUserByUsername(user.getUsername())).thenReturn(user);

        assertDoesNotThrow(() -> commentService.deleteComment(ad.getId(), comment.getId()));
        verify(commentRepository, times(1)).delete(comment);
    }

    @Test
    void deleteComment_ShouldThrowCommentNotFound() {
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.empty());

        assertThrows(CommentNotFound.class, () -> commentService.deleteComment(ad.getId(), comment.getId()));
    }

    @Test
    void updateComment_ShouldUpdateComment() {
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        when(securityUtils.getCurrentUsername()).thenReturn(user.getUsername());
        when(userRepository.findUserByUsername(user.getUsername())).thenReturn(user);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(mappingCommentDTO.mapToDTO(any(Comment.class))).thenAnswer(invocation -> {
            Comment updatedComment = invocation.getArgument(0);
            return new CommentDTO(updatedComment.getId(), "authorImage",
                    "authorFirstName", Instant.MIN, updatedComment.getAd().getId(), updatedComment.getText());
        });

        CreateOrUpdateCommentDTO dto = new CreateOrUpdateCommentDTO("updatedComment");
        CommentDTO result = commentService.updateComment(ad.getId(), comment.getId(), dto);

        assertEquals("updatedComment", result.getText());
    }

    @Test
    void updateComment_ShouldThrowCommentNotFound() {
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.empty());

        CreateOrUpdateCommentDTO dto = new CreateOrUpdateCommentDTO("updatedComment");
        assertThrows(CommentNotFound.class, () -> commentService.updateComment(ad.getId(), comment.getId(), dto));
    }
}
