package com.inforush.repository;

import com.inforush.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, String> {
    Optional<Tag> findBySlug(String slug);
    boolean existsBySlug(String slug);
}
