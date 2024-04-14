package ru.bulletin_board.bulletin_board.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.bulletin_board.bulletin_board.dtos.ImageDto;
import ru.bulletin_board.bulletin_board.models.FavoritePost;
import ru.bulletin_board.bulletin_board.models.Post;
import ru.bulletin_board.bulletin_board.models.User;
import ru.bulletin_board.bulletin_board.models.ViewedPost;
import ru.bulletin_board.bulletin_board.services.FavoritePostService;
import ru.bulletin_board.bulletin_board.services.PostService;
import ru.bulletin_board.bulletin_board.services.ViewedPostService;
import ru.bulletin_board.bulletin_board.utils.CheckAuthentication;
import ru.bulletin_board.bulletin_board.utils.DataFormatting;
import ru.bulletin_board.bulletin_board.utils.PhoneNumberFormatting;
import ru.bulletin_board.bulletin_board.utils.PriceFormatting;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PostController {
    public static final String HOME_PAGE = "/";
    private final PostService postService;
    private final ViewedPostService viewedPostService;
    private final FavoritePostService favoritePostService;
    @Value("${yandex.maps.apikey}")
    private String YANDEX_MAP_API_KEY;


    @GetMapping(HOME_PAGE)
    public String posts(@RequestParam(name = "heading", required = false) String heading,
                        Model model,
                        Authentication authentication) {
        List<Post> posts = postService.listPosts(heading);
        Map<Long, String> times = DataFormatting.timeFromPosts(posts);
        Map<Long, String> prices = PriceFormatting.priceFromPosts(posts);
        User user = (User) authentication.getPrincipal();
        Map<Long, Boolean> favoriteStatusMap = new HashMap<>();
        for (Post post : posts) {
            boolean isLikedByUser = favoritePostService.isPostLikedByUser(post.getId(), user);
            favoriteStatusMap.put(post.getId(), isLikedByUser);
        }
        List<FavoritePost> favoritePosts = favoritePostService.getFavoritePostsForUser(user);
        List<ViewedPost> viewPosts = viewedPostService.getViewedPostsForUser(user);
        model.addAttribute("favoritePosts", favoritePosts);
        model.addAttribute("viewPosts", viewPosts);
        model.addAttribute("favoriteStatusMap", favoriteStatusMap);
        model.addAttribute("posts", posts);
        model.addAttribute("times", times);
        model.addAttribute("prices", prices);
        model.addAttribute("isAuthenticated", CheckAuthentication.addAuthenticationAttributes(authentication));
        return "index";
    }

    @GetMapping("/post/create")
    public String pageCreatePost(Post post,
                                 Model model,
                                 Authentication authentication) {
        model.addAttribute("isAuthenticated", CheckAuthentication.addAuthenticationAttributes(authentication));
        return "add";
    }

    @PostMapping("/post/create")
    public String createPost(@Valid @ModelAttribute("post") Post post,
                             BindingResult bindingResult,
                             @RequestParam("file") MultipartFile[] multipartFile,
                             Principal principal) throws IOException {
        if (bindingResult.hasErrors()) {
            return "add";
        }
        postService.savePost(multipartFile, post, principal);
        return "redirect:/";
    }

    @PostMapping("/post/delete/{id}")
    public String deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return "redirect:/";
    }

    @GetMapping("/post/{id}")
    public String postInfo(@PathVariable Long id,
                           Model model,
                           Authentication authentication) {
        Post post = postService.getPostById(id);
        User user = (User) authentication.getPrincipal();
        boolean isCurrentUserTheCreator = user.equals(post.getUser());
        if (!isCurrentUserTheCreator) {
            viewedPostService.addToViewHistory(user, post);
            Integer viewsCount = post.getViewsCount();
            int actualViewsCount = (viewsCount != null) ? viewsCount : 0;
            postService.updateViewsCountPost(post, actualViewsCount);
        }
        String price = PriceFormatting.priceFormatting(post.getPrice());
        String phoneNumber = PhoneNumberFormatting.formatPhoneNumber(post.getPhoneNumber());
        List<ImageDto> imageDtos = postService.listImagesDtos(post);
        User postCreator = post.getUser();
        String dateOfCreatedUser = DataFormatting.timeFormatting(postCreator.getDateOfCreated());
        String timePostCreat = DataFormatting.timeFormatting(post.getDateTime());
        model.addAttribute("post", post);
        model.addAttribute("price", price);
        model.addAttribute("phoneNumber", phoneNumber);
        model.addAttribute("images", imageDtos);
        model.addAttribute("user", postCreator);
        model.addAttribute("dateOfCreatedUser", dateOfCreatedUser);
        model.addAttribute("timePostCreat", timePostCreat);
        model.addAttribute("isCurrentUserTheCreator", isCurrentUserTheCreator);
        model.addAttribute("isAuthenticated", CheckAuthentication.addAuthenticationAttributes(authentication));
        model.addAttribute("yandexMapApiKey", YANDEX_MAP_API_KEY);

        return "post-info";
    }

    @GetMapping("/post/{id}/edit")
    public String postEditPage(@PathVariable Long id,
                               Model model,
                               Authentication authentication) {
        Post postEdit = postService.getPostById(id);
        model.addAttribute("postEdit", postEdit);
        model.addAttribute("isAuthenticated", CheckAuthentication.addAuthenticationAttributes(authentication));
        return "post-edit";
    }

    @PostMapping("/post/{id}/edit")
    public String createEdit(@PathVariable("id") Long id,
                             @RequestParam String heading,
                             @RequestParam String description,
                             @RequestParam int price,
                             @RequestParam String city,
                             @RequestParam String street,
                             @RequestParam String houseNumber,
                             @RequestParam String phoneNumber,
                             @RequestParam("file") MultipartFile[] multipartFile,
                             Principal principal) throws IOException {
        Post postEdit = postService.getPostById(id);
        postEdit.setHeading(heading);
        postEdit.setDescription(description);
        postEdit.setPrice(price);
        postEdit.setCity(city);
        postEdit.setStreet(street);
        postEdit.setHouseNumber(houseNumber);
        postEdit.setPhoneNumber(phoneNumber);
        postService.savePost(multipartFile, postEdit, principal); //TODO: пост не сохраняется (400)
        return "redirect:/";
    }

    @GetMapping("/post/favorites")
    public String showFavoritePosts(Model model, Principal principal) {
        List<Post> posts = postService.getPosts();
        User currentUser = (User) ((Authentication) principal).getPrincipal();
        Map<Long, String> prices = PriceFormatting.priceFromPosts(posts);
        List<Post> favoritePosts = favoritePostService.getAllFavoritePosts(currentUser);
        List<ViewedPost> viewPosts = viewedPostService.getViewedPostsForUser(currentUser);
        Map<Long, Boolean> favoriteStatusMap = new HashMap<>();
        for (Post post : posts) {
            boolean isLikedByUser = favoritePostService.isPostLikedByUser(post.getId(), currentUser);
            favoriteStatusMap.put(post.getId(), isLikedByUser);
        }
        model.addAttribute("favoritePosts", favoritePosts);
        model.addAttribute("favoriteStatusMap", favoriteStatusMap);
        model.addAttribute("viewPosts", viewPosts);
        model.addAttribute("prices", prices);
        return "favourites";
    }
}
