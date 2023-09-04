package com.rest.service.implementations;


import com.rest.model.Comment;
import com.rest.repository.CommentRepository;
import com.rest.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {
    CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment findById(Integer id) {
        return commentRepository.findById(id).get();
    }

    @Override
    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public void deleteById(Integer id) {
        commentRepository.deleteById(id);
    }

    @Override
    public List<Comment> findAllBYPostId(Integer postId) {
        List<Comment> comments = commentRepository.findAllByPostId(postId);
        return comments;
    }
}
