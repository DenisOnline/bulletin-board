package ru.bulletin_board.bulletin_board.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.bulletin_board.bulletin_board.models.Post;
import ru.bulletin_board.bulletin_board.models.User;
import ru.bulletin_board.bulletin_board.services.FavoritePostService;
import ru.bulletin_board.bulletin_board.services.PostService;

@Controller
@RequiredArgsConstructor
public class FavoritePostController {
    private final FavoritePostService favoritePostService;
    private final PostService postService;

    @PostMapping("/like")
    public ModelAndView likePost(@RequestParam Long postId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Post post = postService.getPostById(postId);

        boolean isLikedByUser = favoritePostService.isPostLikedByUser(postId, user);

        if (!isLikedByUser) {
            favoritePostService.addPostToFavorites(user, post);
        } else {
            favoritePostService.removePostFromFavorites(user, post);
        }

        return new ModelAndView("redirect:/");
    }
}
