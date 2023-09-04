package com.rest.service;


import com.rest.model.Comment;

import java.util.List;

public interface CommentService {
    Comment findById(Integer id);

    void save(Comment comment);

    void deleteById(Integer id);

    List<Comment> findAllBYPostId(Integer postId);
}
