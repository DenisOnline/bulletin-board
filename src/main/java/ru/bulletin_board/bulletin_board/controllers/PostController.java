package ru.bulletin_board.bulletin_board.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.bulletin_board.bulletin_board.dtos.ImageDto;
import ru.bulletin_board.bulletin_board.models.Post;
import ru.bulletin_board.bulletin_board.services.PostService;
import ru.bulletin_board.bulletin_board.utils.DataFormatting;
import ru.bulletin_board.bulletin_board.utils.PhoneNumberFormatting;
import ru.bulletin_board.bulletin_board.utils.PriceFormatting;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/")
    public String posts(@RequestParam(name = "heading", required = false) String heading, Model model) {
        List<Post> posts = postService.listPosts(heading);
        Map<Long, String> times = DataFormatting.timeFromPosts(posts);
        Map<Long, String> prices = PriceFormatting.priceFromPosts(posts);
        model.addAttribute("posts", posts);
        model.addAttribute("times", times);
        model.addAttribute("prices", prices);
        return "index";
    }

    @GetMapping("/post/create")
    public String pageCreatePost(Post post) {
        return "add";
    }

    @PostMapping("/post/create")
    public String createPost(@Valid @ModelAttribute("post") Post post,
                             BindingResult bindingResult,
                             @RequestParam("file") MultipartFile[] multipartFile) throws IOException {
        if (bindingResult.hasErrors()) {
            return "add";
        }
        postService.savePost(multipartFile, post);
        return "redirect:/";
    }

    @PostMapping("/post/delete/{id}")
    public String deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return "redirect:/";
    }

    @GetMapping("/post/{id}")
    public String postInfo(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);
        String price = PriceFormatting.priceFormatting(post.getPrice());
        List<ImageDto> imageDtos = postService.listImagesDtos(post);
        String phoneNumber = PhoneNumberFormatting.formatPhoneNumber(post.getPhoneNumber());
        model.addAttribute("post", post);
        model.addAttribute("price", price);
        model.addAttribute("phoneNumber", phoneNumber);
        model.addAttribute("images", imageDtos);
        return "post-info";
    }
}
