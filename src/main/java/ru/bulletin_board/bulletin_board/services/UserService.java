package ru.bulletin_board.bulletin_board.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.bulletin_board.bulletin_board.models.User;
import ru.bulletin_board.bulletin_board.models.enums.Role;
import ru.bulletin_board.bulletin_board.repositories.UserRepository;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean createNewUser(User user) {
        String email = user.getEmail();
        if (userRepository.findUserByEmail(email) != null) {
            return false;
        }
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(Role.USER);
        log.info("Saving new user with email: {}", email);
        userRepository.save(user);
        return true;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void banUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            if (user.isActive()) {
                user.setActive(false);
                log.info("Ban user with id = {}; email: {}", user.getId(), user.getEmail());
            } else {
                user.setActive(true);
                log.info("Unban user with id = {}; email: {}", user.getId(), user.getEmail());
            }
        }
        userRepository.save(user);
    }

    public void changeUserRole(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            if (!user.isAdmin()) {
                user.getRoles().remove(Role.USER);
                user.getRoles().add(Role.ADMIN);
                log.info("change user id: {}; role: {}", user.getId(), user.getRoles());
            } else {
                user.getRoles().remove(Role.ADMIN);
                user.getRoles().add(Role.USER);
                log.info("change user id: {}; role: {}", user.getId(), user.getRoles());
            }
        }
        userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}

