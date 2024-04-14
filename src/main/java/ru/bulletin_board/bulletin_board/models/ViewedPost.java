package ru.bulletin_board.bulletin_board.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "viewed_posts")
@AllArgsConstructor
@NoArgsConstructor
public class ViewedPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "viewed_at")
    private LocalDateTime viewedAt;

    public ViewedPost(User user, Post post, LocalDateTime now) {
        this.user = user;
        this.post = post;
        this.viewedAt = now;
    }
}