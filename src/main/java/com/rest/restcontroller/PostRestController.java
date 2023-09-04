package com.rest.restcontroller;

import com.rest.dto.PostRequestDTO;
import com.rest.model.Post;
import com.rest.model.Tag;
import com.rest.service.CommentService;
import com.rest.service.PostService;
import com.rest.service.TagService;
import com.rest.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api")
public class PostRestController {
    PostService postService;
    TagService tagService;
    CommentService commentService;
    UserService userService;

    @Autowired
    public PostRestController(PostService postService, TagService tagService, CommentService commentService, UserService userService) {
        this.postService = postService;
        this.tagService = tagService;
        this.commentService = commentService;
        this.userService = userService;
    }
    @GetMapping("/posts")
    public ResponseEntity<Map<String,Object>> getAllPosts(@RequestParam(defaultValue = "1") Integer pageNo,
                                                  @RequestParam(defaultValue = "10")Integer pageSize,
                                                  @RequestParam(defaultValue = "createdAt") String sortField,
                                                  @RequestParam(defaultValue = "desc") String sortDirection,
                                                  @RequestParam(value = "search", required = false)String searchRequest,
                                                  @RequestParam(value = "selectedAuthors", required = false) Set<String> selectedAuthors,
                                                  @RequestParam(value ="selectedTags", required = false) Set<String> selectedTags,
                                                  @RequestParam(value="startDate", required = false)LocalDateTime startDate,
                                                  @RequestParam(value="endDate", required = false) LocalDateTime endDate) {
        Sort sort = sortDirection.equals("desc") ?
                Sort.by(Sort.Order.desc(sortField)) : Sort.by(Sort.Order.asc(sortField));
        Pageable pageable = PageRequest.of(pageNo-1, pageSize, sort);

        Page<Post> paginatedPosts = null;

        if(searchRequest != null && !searchRequest.isEmpty()) {
            if((selectedAuthors!=null && !selectedAuthors.isEmpty()) || (selectedTags!=null && !selectedTags.isEmpty()) ||
                    (startDate!=null && endDate!=null)) {
                paginatedPosts = postService.filterAndSearchPosts(searchRequest, selectedAuthors, selectedTags,
                                        startDate, endDate, pageable);
            } else {
                paginatedPosts = postService.findAllPostsBySearchRequest(searchRequest, pageable);
            }
        } else {
            if((selectedAuthors!=null && !selectedAuthors.isEmpty()) || (selectedTags!=null && !selectedTags.isEmpty()) ||
                    (startDate!=null && endDate!=null)) {
                paginatedPosts = postService.filterPosts(selectedAuthors, selectedTags, startDate, endDate, pageable);
            } else {
                paginatedPosts = postService.findAll(pageable);
            }
        }

        List<Post> posts = paginatedPosts.getContent();

        List<PostRequestDTO> dtoPosts =new ArrayList<>();
        for(Post post : posts) {
            PostRequestDTO postRequestDTO = new PostRequestDTO();
            postRequestDTO.setAuthor(post.getAuthor());
            postRequestDTO.setContent(post.getContent());
            postRequestDTO.setExcerpt(post.getExcerpt());
            postRequestDTO.setTags(post.getTags());
            postRequestDTO.setPublished(post.isPublished());
            postRequestDTO.setTitle(post.getTitle());

            dtoPosts.add(postRequestDTO);
        }

        Map<String, Object> resultMap  = new HashMap<>();
        resultMap.put("posts", dtoPosts);
        resultMap.put("currentPage", pageNo);
        resultMap.put("pageSize", pageSize);
        resultMap.put("sortField", sortField);
        resultMap.put("sortDirection", sortDirection);
        resultMap.put("search", searchRequest);
        resultMap.put("totalPages", paginatedPosts.getTotalPages());

        return ResponseEntity.ok(resultMap);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostRequestDTO> getPostByPostId(@PathVariable Integer postId) {
        Post post = postService.findById(postId);

        if(post == null) {
           return  ResponseEntity.notFound().build();
        }

        PostRequestDTO postRequestDTO = new PostRequestDTO();
        postRequestDTO.setTitle(post.getTitle());
        postRequestDTO.setAuthor(post.getAuthor());
        postRequestDTO.setExcerpt(post.getExcerpt());
        postRequestDTO.setTags(post.getTags());
        postRequestDTO.setContent(post.getContent());
        postRequestDTO.setPublished(post.isPublished());

        return ResponseEntity.ok(postRequestDTO);
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<String> updatePost(@PathVariable Integer postId, @RequestBody PostRequestDTO postRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Post post = postService.findById(postId);

        if(authentication.isAuthenticated()) {
            if(!authentication.getName().equals(post.getAuthor())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            if (postRequestDTO.getTitle() != null) {
                post.setTitle(postRequestDTO.getTitle());
            }
            if (postRequestDTO.getExcerpt() != null) {
                post.setExcerpt(postRequestDTO.getExcerpt());
            }
            if (postRequestDTO.getContent() != null) {
                post.setContent(postRequestDTO.getContent());
            }
            if (postRequestDTO.getTags() != null && !postRequestDTO.getTags().isEmpty()) {
                post.setTags(postRequestDTO.getTags());
            }
            post.setPublished(postRequestDTO.isPublished());

            post.setUpdatedAt(LocalDateTime.now());
            postService.save(post);

            return ResponseEntity.status(HttpStatus.OK).build();
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/posts")
    public ResponseEntity<HttpStatus> addPost(@RequestBody PostRequestDTO postRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        boolean isAuthor = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_AUTHOR"));

        if (isAdmin) {
            if (userService.findByName(postRequestDTO.getAuthor()) == null) {
               return ResponseEntity.status(HttpStatus.CONFLICT).build();
            } else {
                postRequestDTO.setAdminName(authentication.getName());
            }
        }

        Post post = new Post();
        post.setTitle(postRequestDTO.getTitle());
        if(isAdmin) {
            post.setAuthor(postRequestDTO.getAuthor());
        }

        if(isAuthor) {
            post.setAuthor(authentication.getName());
        }

        post.setExcerpt(postRequestDTO.getExcerpt());
        post.setContent(postRequestDTO.getContent());
        post.setPublished(postRequestDTO.isPublished());

        List<Tag> tags = new ArrayList<>();
        for(Tag requestTag : postRequestDTO.getTags()) {
            Tag tag = tagService.findByName(requestTag.getName());
            if(tag != null) {
                tags.add(tag);
            } else {
                Tag newTag = new Tag();
                newTag.setName(requestTag.getName());

                tagService.save(newTag);
            }
        }
        post.setTags(tags);
        post.setCreatedAt(LocalDateTime.now());
        if(post.isPublished() == true) {
            post.setPublishedAt(LocalDateTime.now());
        }
        postService.save(post);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<HttpStatus> deletePost(@PathVariable Integer postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAccessible = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN") || authority.getAuthority().equals("ROLE_AUTHOR"));

        Post post = postService.findById(postId);

        if(isAccessible) {
            if(authentication.getName().equals(post.getAuthor())) {
                postService.deleteById(postId);

                return ResponseEntity.status(HttpStatus.OK).build();
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/my-drafts")
    public ResponseEntity<List<PostRequestDTO>> myDrafts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        boolean isAuthor = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_AUTHOR"));

        List<Post> posts = new ArrayList<>();
        if (isAdmin) {
            posts = postService.findAllByIsPublishedFalseAndAdminNameOrderByPublishedAtDesc(username);
        } else {
            posts = postService.findAllByIsPublishedFalseAndAuthorAndAdminNameIsNullOrderByPublishedAtDesc(username);
        }
        List<PostRequestDTO> dtoPosts =new ArrayList<>();
        for(Post post : posts) {
            PostRequestDTO postRequestDTO = new PostRequestDTO();
            postRequestDTO.setAuthor(post.getAuthor());
            postRequestDTO.setContent(post.getContent());
            postRequestDTO.setExcerpt(post.getExcerpt());
            postRequestDTO.setTags(post.getTags());
            postRequestDTO.setPublished(post.isPublished());
            postRequestDTO.setTitle(post.getTitle());

            dtoPosts.add(postRequestDTO);
        }

        return ResponseEntity.ok(dtoPosts);
    }

    @GetMapping("/my-posts")
    public ResponseEntity<List<PostRequestDTO>> myPosts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        boolean isAuthor = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_AUTHOR"));

        List<Post> posts = new ArrayList<>();
        if (isAdmin) {
            posts = postService.findAllByIsPublishedTrueAndAdminNameOrderByPublishedAtDesc(username);
        } else {
            posts = postService.findAllByIsPublishedTrueAndAuthorAndAdminNameIsNullOrderByPublishedAtDesc(username);
        }

        List<PostRequestDTO> dtoPosts =new ArrayList<>();
        for(Post post : posts) {
            PostRequestDTO postRequestDTO = new PostRequestDTO();
            postRequestDTO.setAuthor(post.getAuthor());
            postRequestDTO.setContent(post.getContent());
            postRequestDTO.setExcerpt(post.getExcerpt());
            postRequestDTO.setTags(post.getTags());
            postRequestDTO.setPublished(post.isPublished());
            postRequestDTO.setTitle(post.getTitle());

            dtoPosts.add(postRequestDTO);
        }

        return ResponseEntity.ok(dtoPosts);

    }


}
