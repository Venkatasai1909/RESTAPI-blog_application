package com.rest.restcontroller;

import com.rest.model.Comment;
import com.rest.model.Post;
import com.rest.service.CommentService;
import com.rest.service.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentRestController {
    private CommentService commentService;
    private PostService postService;
    @Autowired
    public CommentRestController(CommentService commentService, PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<Comment>> getAllCommentsOfPost(@PathVariable Integer postId) {
        List<Comment> comments = commentService.findAllBYPostId(postId);

        return ResponseEntity.ok(comments);
    }

    @PostMapping("/posts/{postId}/comment")
    public ResponseEntity<String> addComment(@PathVariable Integer postId, @RequestBody Comment comment) {
        comment.setPostId(postId);

        commentService.save(comment);

        return ResponseEntity.ok("Comment Saved successfully...");
    }

    @PutMapping("/posts/{postId}/comment")
    public ResponseEntity<HttpStatus> updateComment(@PathVariable Integer postId, @RequestBody Comment updatedComment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthor = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_AUTHOR"));

        Post post = postService.findById(postId);
        if(isAuthor) {
            if(post.getAuthor().equals(authentication.getName())) {
                commentService.save(updatedComment);

                return ResponseEntity.status(HttpStatus.OK).build();
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @DeleteMapping("/posts/{postId}/comment/{commentId}")
    public ResponseEntity<HttpStatus> deleteComment(@PathVariable Integer postId, @PathVariable Integer commentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthor = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_AUTHOR"));

        Post post = postService.findById(postId);
        if(isAuthor) {
            if(post.getAuthor().equals(authentication.getName())) {
                commentService.deleteById(commentId);

                return ResponseEntity.status(HttpStatus.OK).build();
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


}
