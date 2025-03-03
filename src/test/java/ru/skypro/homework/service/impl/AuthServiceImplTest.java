package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import ru.skypro.homework.dto.Register;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static ru.skypro.homework.dto.Role.USER;

class AuthServiceImplTest {

    @Mock
    private UserDetailsManager manager;
    @Mock
    private PasswordEncoder encoder;
    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loginSuccess() {
        String username = "testUser";
        String password = "password";
        UserDetails userDetails = User
                .withUsername(username)
                .password("encodedPassword")
                .roles("USER")
                .build();

        when(manager.userExists(username)).thenReturn(true);
        when(manager.loadUserByUsername(username)).thenReturn(userDetails);
        when(encoder.matches(password, userDetails.getPassword())).thenReturn(true);

        boolean result = authServiceImpl.login(username, password);

        assertTrue(result);
        verify(manager).loadUserByUsername(username);
    }

    @Test
    void loginFailureUserNotFound() {
        String username = "nonexistent";

        when(manager.userExists(username)).thenReturn(false);

        boolean result = authServiceImpl.login(username, "password");

        assertFalse(result);
    }

    @Test
    void registerSuccess() {
        Register register = new Register("newUser", "password", "Ivan", "Ivanov", "79999999999", USER);

        when(manager.userExists(register.getUsername())).thenReturn(false);
        when(encoder.encode(register.getPassword())).thenReturn("password");

        boolean result = authServiceImpl.register(register);

        assertTrue(result);
        verify(manager).createUser(any(UserDetails.class));
    }

    @Test
    void registerFailureZUserAlreadyExists() {
        Register register = new Register("newUser", "password", "Ivan", "Ivanov", "79999999999", USER);

        when(manager.userExists(register.getUsername())).thenReturn(true);

        boolean result = authServiceImpl.register(register);

        assertFalse(result);
        verify(manager, never()).createUser(any(UserDetails.class));
    }
}
