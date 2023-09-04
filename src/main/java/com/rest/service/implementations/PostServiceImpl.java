package com.rest.service.implementations;


import com.rest.model.Post;
import com.rest.repository.PostRepository;
import com.rest.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class PostServiceImpl implements PostService {
    PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public Post findById(Integer id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if(optionalPost.isPresent()) {
            Post post = optionalPost.get();

            return post;
        }

        return null;
    }

    @Override
    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAllByIsPublishedTrue(pageable);
    }

    @Override
    public Page<Post> findAllPostsBySearchRequest(String searchRequest, Pageable pageable) {
        return postRepository.findAllPostsBySearchRequest(searchRequest, pageable);
    }

    @Override
    public void deleteById(Integer id) {
        postRepository.deleteById(id);
    }

    @Override
    public void save(Post post) {
        postRepository.save(post);
    }

    @Override
    public Page<Post> filterPosts(Set<String> authorNames,
                                  Set<String> tagNames, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        int authorList = authorNames == null || authorNames.isEmpty() ? 1 : 0;
        int tagList = tagNames == null || tagNames.isEmpty() ? 1 : 0;
        int dates = (startDate == null || endDate == null) ? 1 : 0;

        return postRepository.filterPosts(authorList, tagList, dates,
                authorNames, tagNames, startDate, endDate, pageable);
    }
    @Override
    public List<Post> findAllByIsPublishedFalseAndAdminNameOrderByPublishedAtDesc(String adminName) {
        return postRepository.findAllByIsPublishedFalseAndAdminNameOrderByPublishedAtDesc(adminName);
    }

    @Override
    public List<Post> findAllByIsPublishedFalseAndAuthorAndAdminNameIsNullOrderByPublishedAtDesc(String author) {
        return postRepository.findAllByIsPublishedFalseAndAuthorAndAdminNameIsNullOrderByPublishedAtDesc(author);
    }

    @Override
    public List<Post> findAllByIsPublishedTrueAndAuthorAndAdminNameIsNullOrderByPublishedAtDesc(String author) {
        return postRepository.findAllByIsPublishedTrueAndAuthorAndAdminNameIsNullOrderByPublishedAtDesc(author);
    }

    @Override
    public List<Post> findAllByIsPublishedTrueAndAdminNameOrderByPublishedAtDesc(String adminName) {
        return postRepository.findAllByIsPublishedTrueAndAdminNameOrderByPublishedAtDesc(adminName);
    }

    @Override
    public Page<Post> filterAndSearchPosts(String searchRequest, Set<String> authorNames, Set<String> tagNames, LocalDateTime startDate,
                                           LocalDateTime endDate, Pageable pageable) {
        int authorList = authorNames == null || authorNames.isEmpty() ? 1 : 0;
        int tagList = tagNames == null || tagNames.isEmpty() ? 1 : 0;
        int dates = (startDate == null || endDate == null) ? 1 : 0;

        return postRepository.filterAndSearchPosts(searchRequest, authorList, tagList, dates,
                authorNames, tagNames, startDate, endDate, pageable);

    }

}