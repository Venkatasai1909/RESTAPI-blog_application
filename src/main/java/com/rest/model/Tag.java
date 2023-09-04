package com.rest.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    @JsonIgnore
    private Integer id;
    @Column(name="name")
    private String name;
    @Column(name="created_at")
    @JsonIgnore
    private LocalDateTime createdAt;
    @Column(name="updated_at")
    @JsonIgnore
    private LocalDateTime updatedAt;

    @ManyToMany
    @JoinTable(name="post_tags", joinColumns = @JoinColumn(name="tag_id"),
            inverseJoinColumns = @JoinColumn(name="post_id"))
    private List<Post> posts;
     @JsonIgnore
    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
