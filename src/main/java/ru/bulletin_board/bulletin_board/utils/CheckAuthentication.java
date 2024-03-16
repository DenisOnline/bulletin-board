package ru.bulletin_board.bulletin_board.utils;

import org.springframework.security.core.Authentication;

public class CheckAuthentication {

    public static boolean addAuthenticationAttributes(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated();
    }
}
