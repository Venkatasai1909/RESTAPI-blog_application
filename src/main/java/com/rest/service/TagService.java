package com.rest.service;

import com.rest.model.Tag;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface TagService {
    void save(Tag tag);
    Tag findByName(String tagName);
    Set<String> findTagsWithPublishedPosts(Integer postId);


}
