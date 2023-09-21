package ru.bulletin_board.bulletin_board.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.bulletin_board.bulletin_board.models.Post;
import ru.bulletin_board.bulletin_board.models.User;
import ru.bulletin_board.bulletin_board.services.PostService;
import ru.bulletin_board.bulletin_board.services.UserService;
import ru.bulletin_board.bulletin_board.utils.CheckAuthentication;
import ru.bulletin_board.bulletin_board.utils.DataFormatting;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PostService postService;

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

    @GetMapping("/user/profile/{id}edit")
    public String userEditPage(@PathVariable Long id,
                               Model model,
                               Authentication authentication) {
        User userById = userService.getUserById(id);
        model.addAttribute("userEdit", userById);//TODO: страница не отображается (404)
        model.addAttribute("isAuthenticated", CheckAuthentication.addAuthenticationAttributes(authentication));
        return "user-edit";
    }

    @GetMapping("/user/profile/{id}")
    public String profile(@PathVariable("id") Long id,
                          @RequestParam(name = "heading", required = false) String heading,
                          Model model,
                          Authentication authentication) {
        User user = userService.getUserById(id);
        List<Post> posts = postService.listPosts(heading);
        Map<Long, String> times = DataFormatting.timeFromPosts(posts);
        String dateOfCreatedUser = DataFormatting.timeFormatting(user.getDateOfCreated());
        model.addAttribute("user", user);
        model.addAttribute("times", times);
        model.addAttribute("dateOfCreatedUser", dateOfCreatedUser);
        model.addAttribute("isAuthenticated", CheckAuthentication.addAuthenticationAttributes(authentication));
        return "profile";
    }
}
