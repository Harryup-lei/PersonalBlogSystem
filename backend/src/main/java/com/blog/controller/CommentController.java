package com.blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.common.Result;
import com.blog.entity.Comment;
import com.blog.service.CommentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/add")
    public Result<Comment> addComment(@RequestBody @Valid CommentRequest request) {
        Comment comment = new Comment();
        comment.setArticleId(request.getArticleId());
        comment.setCommentator(request.getCommentator());
        comment.setCommentEmail(request.getCommentEmail());
        comment.setCommentContent(request.getCommentContent());
        Comment added = commentService.addComment(comment);
        return Result.success("评论成功", added);
    }

    @PutMapping("/update")
    public Result<Comment> updateComment(@RequestBody Comment comment) {
        Comment updated = commentService.updateComment(comment);
        return Result.success("更新成功", updated);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return Result.success();
    }

    @GetMapping("/article/{articleId}")
    public Result<Page<Comment>> getArticleComments(
            @PathVariable Long articleId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Comment> page = commentService.getArticleComments(articleId, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/article/{articleId}/all")
    public Result<List<Comment>> getAllArticleComments(@PathVariable Long articleId) {
        List<Comment> comments = commentService.getAllArticleComments(articleId);
        return Result.success(comments);
    }
}

@Data
class CommentRequest {
    private Long articleId;
    @NotBlank(message = "评论人不能为空")
    private String commentator;
    private String commentEmail;
    @NotBlank(message = "评论内容不能为空")
    private String commentContent;
}
