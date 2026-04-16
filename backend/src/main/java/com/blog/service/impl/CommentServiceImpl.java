package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.entity.Comment;
import com.blog.mapper.CommentMapper;
import com.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public Comment addComment(Comment comment) {
        comment.setLikeCount(0L);
        comment.setStatus(1);
        comment.setIsDeleted(0);
        commentMapper.insert(comment);
        return comment;
    }

    @Override
    public Comment updateComment(Comment comment) {
        commentMapper.updateById(comment);
        return comment;
    }

    @Override
    public void deleteComment(Long commentId) {
        commentMapper.deleteById(commentId);
    }

    @Override
    public Page<Comment> getArticleComments(Long articleId, Integer pageNum, Integer pageSize) {
        Page<Comment> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getArticleId, articleId);
        wrapper.eq(Comment::getStatus, 1);
        wrapper.eq(Comment::getIsDeleted, 0);
        wrapper.orderByDesc(Comment::getCreateTime);
        return commentMapper.selectPage(page, wrapper);
    }

    @Override
    public List<Comment> getAllArticleComments(Long articleId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getArticleId, articleId);
        wrapper.eq(Comment::getStatus, 1);
        wrapper.eq(Comment::getIsDeleted, 0);
        wrapper.orderByDesc(Comment::getCreateTime);
        return commentMapper.selectList(wrapper);
    }
}
