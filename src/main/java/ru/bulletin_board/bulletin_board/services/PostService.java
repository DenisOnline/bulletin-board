package ru.bulletin_board.bulletin_board.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.bulletin_board.bulletin_board.dtos.ImageDto;
import ru.bulletin_board.bulletin_board.models.Image;
import ru.bulletin_board.bulletin_board.models.Post;
import ru.bulletin_board.bulletin_board.repositories.ImageRepository;
import ru.bulletin_board.bulletin_board.repositories.PostRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final DateTimeFormatter DATA_FORMAT = DateTimeFormatter.ofPattern("dd MMMM, HH:mm", new Locale("ru"));

    public List<Post> listPosts(String heading) {
        if (heading != null) {
            postRepository.findByHeading(heading);
        }
        return postRepository.findAll();
    }

    public void savePost(MultipartFile[] multipartFile, Post post) throws IOException {
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

    public String timeFormatting(LocalDateTime data) {
        return data.format(DATA_FORMAT);
    }

    public Map<Long, String> timeFromPosts(List<Post> posts) {
        Map<Long, String> dataMap = new HashMap<>();
        posts.forEach(post -> {
            dataMap.put(post.getId(), timeFormatting(post.getDateTime()));
        });
        return dataMap;
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

    @Transactional
    public List<ImageDto> listImagesDtos(Post post) {
        List<ImageDto> imageDtos = new ArrayList<>(imageRepository.findAllByPost(post).stream().map(i -> new ImageDto(i.getId())).toList());
        imageDtos.remove(0);
        return imageDtos;
    }
}