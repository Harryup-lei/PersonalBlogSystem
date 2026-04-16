package com.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.entity.Tag;

import java.util.List;

public interface TagService {

    Tag createTag(Tag tag);

    Tag updateTag(Tag tag);

    void deleteTag(Long tagId);

    List<Tag> getAllTags();

    Page<Tag> getTagList(Integer pageNum, Integer pageSize);
}
