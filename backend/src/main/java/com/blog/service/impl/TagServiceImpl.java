package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.common.BizException;
import com.blog.common.ResponseCode;
import com.blog.entity.Tag;
import com.blog.mapper.TagMapper;
import com.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public Tag createTag(Tag tag) {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Tag::getTagName, tag.getTagName());
        if (tagMapper.selectCount(wrapper) > 0) {
            throw new BizException(ResponseCode.PARAM_ERROR.getCode(), "标签名称已存在");
        }
        tag.setIsDeleted(0);
        tag.setSort(0);
        tagMapper.insert(tag);
        return tag;
    }

    @Override
    public Tag updateTag(Tag tag) {
        Tag existingTag = tagMapper.selectById(tag.getId());
        if (existingTag == null) {
            throw new BizException(ResponseCode.NOT_FOUND.getCode(), "标签不存在");
        }
        tagMapper.updateById(tag);
        return tag;
    }

    @Override
    public void deleteTag(Long tagId) {
        tagMapper.deleteById(tagId);
    }

    @Override
    public List<Tag> getAllTags() {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Tag::getIsDeleted, 0);
        wrapper.orderByAsc(Tag::getSort);
        return tagMapper.selectList(wrapper);
    }

    @Override
    public Page<Tag> getTagList(Integer pageNum, Integer pageSize) {
        Page<Tag> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Tag::getIsDeleted, 0);
        wrapper.orderByAsc(Tag::getSort);
        return tagMapper.selectPage(page, wrapper);
    }
}
