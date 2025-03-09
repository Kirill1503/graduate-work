package ru.skypro.homework.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsManager implements UserDetailsManager {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createUser(UserDetails userDetails) {
        User user = new User();
        user.setUsername(userDetails.getUsername());
        user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void updateUser(UserDetails userDetails) {
        User user = userRepository.findUserByUsername(userDetails.getUsername());
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void deleteUser(String username) {
        User user = userRepository.findUserByUsername(username);
        if (user != null) {
            userRepository.delete(user);
        }
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        User user = userRepository.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (user == null || !passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Invalid old password");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public boolean userExists(String username) {
        return userRepository.findUserByUsername(username) != null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}

