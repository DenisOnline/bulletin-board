package ru.bulletin_board.bulletin_board.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.bulletin_board.bulletin_board.models.User;
import ru.bulletin_board.bulletin_board.services.UserService;
import ru.bulletin_board.bulletin_board.utils.CheckAuthentication;

@Controller
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService userService;
    @GetMapping("/registration")
    public String registration(User user,
                               Model model,
                               Authentication authentication) {
        model.addAttribute("isAuthenticated", CheckAuthentication.addAuthenticationAttributes(authentication));
        return "registration";
    }

    @GetMapping("/login")
    public String login(Model model,
                        Authentication authentication) {
        model.addAttribute("isAuthenticated", CheckAuthentication.addAuthenticationAttributes(authentication));
        return "login";
    }

    @PostMapping("/registration")
    public String createUser(@Valid @ModelAttribute("user") User user,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        if (!userService.createNewUser(user)) {
            model.addAttribute("errorMassage", "Пользователь с email: " + user.getEmail() + " уже существует");
            return "redirect:/";
        }
        return "redirect:/";
    }
}
