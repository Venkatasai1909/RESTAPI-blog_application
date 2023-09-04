package com.rest.service;


import com.rest.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface PostService {
    List<Post> findAll();
    Post findById(Integer id);
    Page<Post> findAll(Pageable pageable);
    Page<Post> findAllPostsBySearchRequest(String search, Pageable pageable);
    void deleteById(Integer id);
    void save(Post post);
    Page<Post> filterPosts(Set<String> authors,
                           Set<String> tagNames, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    List<Post> findAllByIsPublishedFalseAndAdminNameOrderByPublishedAtDesc(String adminName);
    List<Post> findAllByIsPublishedFalseAndAuthorAndAdminNameIsNullOrderByPublishedAtDesc(String author);

    List<Post> findAllByIsPublishedTrueAndAuthorAndAdminNameIsNullOrderByPublishedAtDesc(String author);
    List<Post> findAllByIsPublishedTrueAndAdminNameOrderByPublishedAtDesc(String adminName);
    Page<Post> filterAndSearchPosts(String searchRequest, Set<String> authors, Set<String> tagNames, LocalDateTime startDate,
                                    LocalDateTime endDate, Pageable pageable);


}