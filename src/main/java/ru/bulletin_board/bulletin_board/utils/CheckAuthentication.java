package ru.bulletin_board.bulletin_board.utils;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

public class CheckAuthentication {

    public static boolean addAuthenticationAttributes(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return true;
        } else {
            return false;
        }
    }
}
