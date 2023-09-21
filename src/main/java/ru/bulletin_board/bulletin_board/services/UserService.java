package ru.bulletin_board.bulletin_board.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.bulletin_board.bulletin_board.models.User;
import ru.bulletin_board.bulletin_board.models.enums.Role;
import ru.bulletin_board.bulletin_board.repositories.UserRepository;


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
}

