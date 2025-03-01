package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.GetUserDTO;
import ru.skypro.homework.dto.UpdateUserDTO;
import ru.skypro.homework.exception.ThePasswordIsNotTrue;
import ru.skypro.homework.exception.TheUserIsNotAuthenticated;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.utils.SecurityUtils;

import java.io.IOException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @TempDir
    private Path tempDir;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setPassword("encodedPassword");
    }

    @AfterEach
    void tearDown() {
        user = null;
    }

    @Test
    void updateUserPasswordNegativeTest() {
        when(securityUtils.getCurrentUsername()).thenReturn("testUser");
        when(userRepository.findUserByUsername("testUser")).thenReturn(user);
        when(encoder.matches("wrongPassword", user.getPassword())).thenReturn(false);

        assertThatExceptionOfType(ThePasswordIsNotTrue.class)
                .isThrownBy(() -> userService.updateUserPassword("wrongPassword", "newPassword"));
    }

    @Test
    void updateUserPasswordPositiveTest() {
        when(securityUtils.getCurrentUsername()).thenReturn("testUser");
        when(userRepository.findUserByUsername("testUser")).thenReturn(user);
        when(encoder.matches("oldPassword", user.getPassword())).thenReturn(true);
        when(encoder.encode("newPassword")).thenReturn("newEncodedPassword");

        userService.updateUserPassword("oldPassword", "newPassword");

        assertEquals("newEncodedPassword", user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void updateUserPasswordNegativeTestWhenUserIsNotAuthenticated() {
        when(securityUtils.getCurrentUsername()).thenReturn(null);

        assertThatExceptionOfType(TheUserIsNotAuthenticated.class)
                .isThrownBy(() -> userService.updateUserPassword("oldPassword", "newPassword"));
    }

    @Test
    void getUserInformationReturnUserDTOTest() {
        when(securityUtils.getCurrentUsername()).thenReturn("testUser");
        when(userRepository.findUserByUsername("testUser")).thenReturn(user);

        GetUserDTO userDTO = userService.getUserInformation();

        assertThat(userDTO).isNotNull();
        assertEquals("testUser", user.getUsername());
    }

    @Test
    void updateUserInformationReturnUserDTOTest() {
        when(securityUtils.getCurrentUsername()).thenReturn("testUser");
        when(userRepository.findUserByUsername("testUser")).thenReturn(user);

        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setFirstName("newFirstName");
        updateUserDTO.setLastName("newLastName");
        updateUserDTO.setPhone("79999999999");

        UpdateUserDTO updatedUserDTO = userService.updateUserInformation(updateUserDTO);

        assertEquals("newFirstName", updatedUserDTO.getFirstName());
        assertEquals("newLastName", updatedUserDTO.getLastName());
        assertEquals("79999999999", updatedUserDTO.getPhone());
        verify(userRepository).save(user);
    }

    @Test
    void updateUserInformationReturnUserDTONegativeTest() {
        when(securityUtils.getCurrentUsername()).thenReturn(null);

        assertThatExceptionOfType(TheUserIsNotAuthenticated.class)
                .isThrownBy(() -> userService.updateUserInformation(new UpdateUserDTO()));
    }

    @Test
    void updateUserAvatarNegativeTest() {
        MultipartFile emptyFile = mock(MultipartFile.class);
        when(emptyFile.isEmpty()).thenReturn(true);

        when(securityUtils.getCurrentUsername()).thenReturn("testUser");
        when(userRepository.findUserByUsername("testUser")).thenReturn(null);

        assertThrows(TheUserIsNotAuthenticated.class, () -> userService.updateUserAvatar(emptyFile));
    }

    @Test
    void updateUserAvatarPositiveTest() throws IOException {
        when(securityUtils.getCurrentUsername()).thenReturn("testUser");
        when(userRepository.findUserByUsername("testUser")).thenReturn(user);

        MultipartFile avatar = mock(MultipartFile.class);
        byte[] fileContent = "test image data".getBytes();
        when(avatar.isEmpty()).thenReturn(false);
        when(avatar.getBytes()).thenReturn(fileContent);

        UserServiceImpl.AVATAR_DIR = tempDir.toString() + "/";

        userService.updateUserAvatar(avatar);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertNotNull(savedUser.getImage());
        assertTrue(savedUser.getImage().startsWith("avatar_"));
    }

    @Test
    void getAuthenticatedUser_NoAuthenticatedUser_ShouldThrowException() {
        when(securityUtils.getCurrentUsername()).thenReturn(null);

        assertThrows(TheUserIsNotAuthenticated.class, userService::getAuthenticatedUser);
    }
}