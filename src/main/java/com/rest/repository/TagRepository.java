package com.rest.repository;


import com.rest.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    Tag findByName(String Name);
    @Query("SELECT t.name FROM Tag t JOIN t.posts p WHERE p.id = :postId AND p.isPublished = true")
    Set<String> findTagsWithPublishedPosts(@Param("postId") Integer postId);

}
