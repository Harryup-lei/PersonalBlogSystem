package com.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.entity.Article;

import java.util.List;

public interface ArticleService {

    Article publish(Article article, List<Long> tagIds);

    Article updateArticle(Article article, List<Long> tagIds);

    void deleteArticle(Long articleId);

    Article getArticleDetail(Long articleId);

    Page<Article> getArticleList(Integer pageNum, Integer pageSize, Long categoryId, String keyword);

    void incrementViewCount(Long articleId);
}
