package ru.bulletin_board.bulletin_board.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bulletin_board.bulletin_board.models.FavoritePost;
import ru.bulletin_board.bulletin_board.models.Post;
import ru.bulletin_board.bulletin_board.models.User;

import java.util.List;

@Repository
public interface FavoritePostRepository extends JpaRepository<FavoritePost, Long> {
    List<FavoritePost> findByUser(User user);
    boolean existsByPostId(Long postId);
    void deleteByPostId(Long postId);

    boolean existsByPostIdAndUserId(Long postId, Long id);

    FavoritePost findByUserAndPost(User user, Post post);
}
