package ru.bulletin_board.bulletin_board.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bulletin_board.bulletin_board.models.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

    void findByHeading(String heading);
    Post searchForPostByHeading(String heading);

}
