package ru.bulletin_board.bulletin_board.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.bulletin_board.bulletin_board.models.User;
import ru.bulletin_board.bulletin_board.services.UserService;
import ru.bulletin_board.bulletin_board.utils.DataFormatting;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    } //TODO: не отображает страницу

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/registration")
    public String createUser(@Valid @ModelAttribute("user") User user,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        else if (!userService.createNewUser(user)) {
            model.addAttribute("errorMassage", "Пользователь с email: " + user.getEmail() + " уже существует");
            return "redirect:/";
        }
        return "redirect:/";
    }

    @GetMapping("/user/profile/{user}")
    public String profile(@PathVariable("user") User user, Model model) {
        String dateOfCreatedUser = DataFormatting.timeFormatting(user.getDateOfCreated());
        model.addAttribute("user", user);
        model.addAttribute("dateOfCreatedUser", dateOfCreatedUser);
        return "profile";
    }
}
