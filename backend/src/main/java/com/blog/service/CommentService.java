package com.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.entity.Comment;

import java.util.List;

public interface CommentService {

    Comment addComment(Comment comment);

    Comment updateComment(Comment comment);

    void deleteComment(Long commentId);

    Page<Comment> getArticleComments(Long articleId, Integer pageNum, Integer pageSize);

    List<Comment> getAllArticleComments(Long articleId);
}
