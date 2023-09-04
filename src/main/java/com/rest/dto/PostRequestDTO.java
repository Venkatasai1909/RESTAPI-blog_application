package com.rest.dto;
import com.rest.model.Tag;

import java.util.List;

public class PostRequestDTO {
    private String title;
    private String author;
    private String excerpt;
    private String content;
    private boolean published;
    private List<Tag> tags;

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    private String adminName;

    public PostRequestDTO() {

    }

    public PostRequestDTO(String title, String author, String excerpt, String content, boolean published, List<Tag> tags) {
        this.title = title;
        this.author = author;
        this.excerpt = excerpt;
        this.content = content;
        this.published = published;
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

}
