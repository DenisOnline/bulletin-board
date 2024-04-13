package ru.bulletin_board.bulletin_board.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bulletin_board.bulletin_board.models.Post;
import ru.bulletin_board.bulletin_board.models.User;
import ru.bulletin_board.bulletin_board.models.ViewedPost;
import ru.bulletin_board.bulletin_board.repositories.ViewedPostRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ViewedPostService {
    private final ViewedPostRepository viewedPostRepository;

    public void addToViewHistory(User user, Post post) {
        ViewedPost existingHistory = viewedPostRepository.findByUserAndPost(user, post);

        if (existingHistory != null) {
            existingHistory.setViewedAt(LocalDateTime.now());
        } else {
            ViewedPost newHistory = new ViewedPost(user, post, LocalDateTime.now());
            viewedPostRepository.save(newHistory);
        }
    }

    public List<ViewedPost> getViewedPostsForUser(User user) {
        return viewedPostRepository.findByUserOrderByViewedAtDesc(user);
    }
}