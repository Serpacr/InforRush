package com.inforush.repository;

import com.inforush.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, String> {
    Optional<Post> findBySlug(String slug);
    boolean existsBySlug(String slug);
    Page<Post> findByPublishedTrue(Pageable pageable);

    @Query("SELECT p FROM Post p JOIN p.tags t WHERE t.slug = :tagSlug AND p.published = true")
    Page<Post> findByTagSlug(@Param("tagSlug") String tagSlug, Pageable pageable);
}
