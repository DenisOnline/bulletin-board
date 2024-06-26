package ru.bulletin_board.bulletin_board.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bulletin_board.bulletin_board.models.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    void findByHeading(String heading);
}
