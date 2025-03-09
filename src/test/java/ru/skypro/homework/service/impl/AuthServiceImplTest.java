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
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.service.impl.AuthServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
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
        String email = "testUser@gmail.com";
        String password = "password";
        UserDetails userDetails = User
                .withUsername(email)
                .password("encodedPassword")
                .roles("USER")
                .build();

        when(manager.userExists(email)).thenReturn(true);
        when(manager.loadUserByUsername(email)).thenReturn(userDetails);
        when(encoder.matches(password, userDetails.getPassword())).thenReturn(true);

        boolean result = authServiceImpl.login(email, password);

        assertTrue(result);
        verify(manager).loadUserByUsername(email);
    }

    @Test
    void loginFailureUserNotFound() {
        String email = "nonexistent@gmail.com";

        when(manager.userExists(email)).thenReturn(false);

        boolean result = authServiceImpl.login(email, "password");

        assertFalse(result);
    }

    @Test
    void registerSuccess() {
        // Конструктор: new Register(firstName, lastName, email, password, role)
        Register register = new Register("Ivan", "Ivanov", "newUser@gmail.com", "password", USER);

        when(manager.userExists(register.getEmail())).thenReturn(false);
        when(encoder.encode(register.getPassword())).thenReturn("encodedPassword");

        boolean result = authServiceImpl.register(register);

        assertTrue(result);
        verify(manager).createUser(any(UserDetails.class));
    }

    @Test
    void registerFailureUserAlreadyExists() {
        Register register = new Register("Ivan", "Ivanov", "newUser@gmail.com", "password", USER);

        when(manager.userExists(register.getEmail())).thenReturn(true);

        boolean result = authServiceImpl.register(register);

        assertFalse(result);
        verify(manager, never()).createUser(any(UserDetails.class));
    }
}
