package com.blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.common.Result;
import com.blog.entity.Article;
import com.blog.service.ArticleService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping("/publish")
    public Result<Article> publish(@RequestBody ArticleRequest request) {
        Article article = new Article();
        article.setArticleTitle(request.getArticleTitle());
        article.setArticleCover(request.getArticleCover());
        article.setCategoryId(request.getCategoryId());
        article.setArticleSummary(request.getArticleSummary());
        article.setMdFilePath(request.getMdFilePath());
        article.setIsTop(request.getIsTop() != null ? request.getIsTop() : 0);
        article.setStatus(request.getStatus() != null ? request.getStatus() : 1);

        Article published = articleService.publish(article, request.getTagIds());
        return Result.success("发布成功", published);
    }

    @PutMapping("/update")
    public Result<Article> updateArticle(@RequestBody ArticleRequest request) {
        Article article = new Article();
        article.setId(request.getId());
        article.setArticleTitle(request.getArticleTitle());
        article.setArticleCover(request.getArticleCover());
        article.setCategoryId(request.getCategoryId());
        article.setArticleSummary(request.getArticleSummary());
        article.setMdFilePath(request.getMdFilePath());
        article.setIsTop(request.getIsTop());
        article.setStatus(request.getStatus());

        Article updated = articleService.updateArticle(article, request.getTagIds());
        return Result.success("更新成功", updated);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<Article> getArticleDetail(@PathVariable Long id) {
        Article article = articleService.getArticleDetail(id);
        articleService.incrementViewCount(id);
        return Result.success(article);
    }

    @GetMapping("/list")
    public Result<Page<Article>> getArticleList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword) {
        Page<Article> page = articleService.getArticleList(pageNum, pageSize, categoryId, keyword);
        return Result.success(page);
    }
}

@Data
class ArticleRequest {
    private Long id;
    private String articleTitle;
    private String articleCover;
    private Long categoryId;
    private String articleSummary;
    private String mdFilePath;
    private Integer isTop;
    private Integer status;
    private List<Long> tagIds;
}
