package ru.bulletin_board.bulletin_board.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.bulletin_board.bulletin_board.models.FavoritePost;
import ru.bulletin_board.bulletin_board.models.Post;
import ru.bulletin_board.bulletin_board.models.User;
import ru.bulletin_board.bulletin_board.repositories.FavoritePostRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FavoritePostService {
    private final FavoritePostRepository favoritePostRepository;
    public void addPostToFavorites(User user, Post post) {
        FavoritePost favoritePost = new FavoritePost(user, post);
        if (post.getLikesCount() == null) {
            post.setLikesCount(0);
        }
        post.setLikesCount(post.getLikesCount() + 1);
        favoritePostRepository.save(favoritePost);
    }

    public List<FavoritePost> getFavoritePostsForUser(User user) {
        return favoritePostRepository.findByUser(user);
    }

    public boolean isPostInFavorites(Long postId) {
        return favoritePostRepository.existsByPostId(postId);
    }


    public Map<Long, Boolean> isFavorite(List<Post> posts) {
        Map<Long, Boolean> favoriteStatusMap = new HashMap<>();
        for (Post post : posts) {
            boolean isFavorite = isPostInFavorites(post.getId());
            favoriteStatusMap.put(post.getId(), isFavorite);
        }
        return favoriteStatusMap;
    }

    public List<Post> getAllFavoritePosts(User user) {
        List<FavoritePost> favoritePosts = favoritePostRepository.findByUser(user);
        // Преобразование списка FavoritePost в список Post для отображения в контроллере
        return favoritePosts.stream()
                .map(FavoritePost::getPost)
                .collect(Collectors.toList());
    }

    public boolean isPostLikedByUser(Long postId, User user) {
        List<FavoritePost> favoritePosts = favoritePostRepository.findByUser(user);
        for (FavoritePost favoritePost : favoritePosts) {
            if (favoritePost.getPost().getId().equals(postId)) {
                return true;
            }
        }
        return false;
    }

    @Transactional
    public void removePostFromFavorites(User user, Post post) {
        FavoritePost favoritePost = favoritePostRepository.findByUserAndPost(user, post);
        if (favoritePost != null) {
            post.setLikesCount(post.getLikesCount() - 1);
            favoritePostRepository.delete(favoritePost);
        }
    }
}
