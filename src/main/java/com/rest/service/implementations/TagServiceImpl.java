package com.rest.service.implementations;

import com.rest.model.Tag;
import com.rest.repository.PostRepository;
import com.rest.repository.TagRepository;
import com.rest.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class TagServiceImpl implements TagService {
    TagRepository tagRepository;
    PostRepository postRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, PostRepository postRepository) {
        this.tagRepository = tagRepository;
        this.postRepository = postRepository;
    }

    @Override
    public void save(Tag tag) {
        tagRepository.save(tag);
    }

    @Override
    public Tag findByName(String tagName) {
        Tag tag = tagRepository.findByName(tagName);
        if (tag == null) {
            Tag newTag = new Tag();
            newTag.setName(tagName);
            return newTag;
        }

        return tag;
    }

    @Override
    public Set<String> findTagsWithPublishedPosts(Integer postId) {
        return tagRepository.findTagsWithPublishedPosts(postId);
    }

}
