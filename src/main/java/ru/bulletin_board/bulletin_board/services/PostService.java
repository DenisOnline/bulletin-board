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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

    public void savePost(MultipartFile multipartFile, Post post) throws IOException {
        Image image = new Image();
        if (!multipartFile.isEmpty()) {
            image.setName(multipartFile.getName());
            image.setOriginalFileName(multipartFile.getOriginalFilename());
            image.setContentType(multipartFile.getContentType());
            image.setSize(String.valueOf(multipartFile.getSize()));
            image.setBytes(multipartFile.getBytes());
            post.addImageToPost(image);
        }
        log.info("Saving new Post. Title: {}" , post.getHeading());
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

    @Transactional
    public List<ImageDto> listImagesDtos(Post post) {
        return imageRepository.findAllByPost(post).stream().map(i -> new ImageDto(i.getId())).toList();
    }

}
