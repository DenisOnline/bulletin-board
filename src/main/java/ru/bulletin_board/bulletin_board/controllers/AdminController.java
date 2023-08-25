package ru.bulletin_board.bulletin_board.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.bulletin_board.bulletin_board.models.Post;
import ru.bulletin_board.bulletin_board.models.User;
import ru.bulletin_board.bulletin_board.models.enums.Role;
import ru.bulletin_board.bulletin_board.services.PostService;
import ru.bulletin_board.bulletin_board.services.UserService;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    private final UserService userService;
    private final PostService postService;

    @GetMapping("/admin/user")
    public String adminPanelUser(Model model) {
        model.addAttribute("users", userService.getUsers());
        return "admin-panel-user";
    }

    @PostMapping("/admin/user/ban/{id}")
    public String userBan(@PathVariable("id") Long id) {
        userService.banUser(id);
        return "redirect:/admin/user";
    }

    @PostMapping("/admin/user/role/change/{id}")
    public String changeUserRole(@PathVariable("id") Long id) {
        userService.changeUserRole(id);
        return "redirect:/admin/user";
    }

//    @GetMapping("/admin/user/edit/{user}")
//    public String userEdit(@PathVariable("user") User user, Model model) {
//        model.addAttribute("user", user);
//        model.addAttribute("roles", Role.values());
//        return "user-edit";
//    }

//    @PostMapping("/admin/user/edit")
//    public String userEdit(@RequestParam("userId") User user, @RequestParam Map<String, String> form) {
//        userService.changeUserRoles(user, form);
//        return "redirect:/admin/user";
//    }

    @GetMapping("/admin/post")
    public String adminPanelPost(Model model) {
        model.addAttribute("posts", postService.getPosts());
        return "admin-panel-post-access";
    }

    @PostMapping("/admin/post/ban/{id}")
    public String postBan(@PathVariable("id") Long id) {
        postService.banPost(id);
        return "redirect:/admin/post";
    }

    @GetMapping("/admin/post/edit/{post}")
    public String postEdit(@PathVariable("post") Post post, Model model) {
        model.addAttribute("post", post);
        return "post-info";
    }
}



