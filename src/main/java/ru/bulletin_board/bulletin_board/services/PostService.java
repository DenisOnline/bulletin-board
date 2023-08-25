package ru.bulletin_board.bulletin_board.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.bulletin_board.bulletin_board.dtos.ImageDto;
import ru.bulletin_board.bulletin_board.models.Image;
import ru.bulletin_board.bulletin_board.models.Post;
import ru.bulletin_board.bulletin_board.models.User;
import ru.bulletin_board.bulletin_board.repositories.ImageRepository;
import ru.bulletin_board.bulletin_board.repositories.PostRepository;
import ru.bulletin_board.bulletin_board.repositories.UserRepository;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

//    private static final Logger logger = LogManager.getLogger(PostService.class);

    public List<Post> listPosts(String heading) {
        if (heading != null) {
            postRepository.findByHeading(heading);
        }
        return postRepository.findAll();
    }

    public void savePost(MultipartFile[] multipartFile, Post post, Principal principal) throws IOException {
        post.setUser(getUserByPrincipal(principal));
        Image image;
        if (multipartFile.length != 0) {
            for (int i = 0; i < multipartFile.length; i++) {
                if (i == 0) {
                    image = toImageEntity(multipartFile[i]);
                    image.setPreviewImage(true);
                    post.addImageToPost(image);
                } else {
                    image = toImageEntity(multipartFile[i]);
                    post.addImageToPost(image);
                }
            }
        }
        post.setAccess(false);
        log.info("Saving new Post. Title: {}" , post.getHeading());
        Post productFromDb = postRepository.save(post);
        productFromDb.setPreviewImageId(productFromDb.getImages().get(0).getId());
        postRepository.save(post);
    }

    public void deletePost(Long id) {
        log.info("Delete post {}" , postRepository.findById(id));
        postRepository.deleteById(id);
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(String.valueOf(file.getSize()));
        image.setBytes(file.getBytes());
        return image;
    }

    public User getUserByPrincipal(Principal principal) {
        if (principal == null) return new User();
        return userRepository.findUserByEmail(principal.getName());
    }

    @Transactional
    public List<ImageDto> listImagesDtos(Post post) {
        List<ImageDto> imageDtos = new ArrayList<>(imageRepository.findAllByPost(post).stream().map(i -> new ImageDto(i.getId())).toList());
        imageDtos.remove(0);
        return imageDtos;
    }

    public List<Post> getPosts() {
        return postRepository.findAll();
    }

    public void banPost(Long id) {
        Post post = postRepository.findById(id).orElse(null);
        if (post != null) {
            if (post.isAccess()) {
                post.setAccess(false);
                log.info("Ban post with id = {}; heading: {}", post.getId(), post.getHeading());
            } else {
                post.setAccess(true);
                log.info("Unban post with id = {}; heading: {}", post.getId(), post.getHeading());
            }
        }
        postRepository.save(post);
    }
}