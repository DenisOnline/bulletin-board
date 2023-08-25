package ru.bulletin_board.bulletin_board.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bulletin_board.bulletin_board.models.Image;
import ru.bulletin_board.bulletin_board.models.Post;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByPost(Post post);
}