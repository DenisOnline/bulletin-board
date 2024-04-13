package ru.bulletin_board.bulletin_board.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bulletin_board.bulletin_board.models.Post;
import ru.bulletin_board.bulletin_board.models.User;
import ru.bulletin_board.bulletin_board.models.ViewedPost;

import java.util.List;

public interface ViewedPostRepository extends JpaRepository<ViewedPost, Long> {
    List<ViewedPost> findByUser(User user);

    ViewedPost findByUserAndPost(User user, Post post);

    List<ViewedPost> findByUserOrderByViewedAtDesc(User user);
}
