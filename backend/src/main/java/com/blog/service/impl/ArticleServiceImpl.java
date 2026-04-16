package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.common.BizException;
import com.blog.common.ResponseCode;
import com.blog.entity.Article;
import com.blog.entity.ArticleTag;
import com.blog.mapper.ArticleMapper;
import com.blog.mapper.ArticleTagMapper;
import com.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    @Transactional
    public Article publish(Article article, List<Long> tagIds) {
        article.setViewCount(0L);
        article.setLikeCount(0L);
        article.setIsTop(0);
        article.setStatus(1);
        article.setIsDeleted(0);
        articleMapper.insert(article);

        if (tagIds != null && !tagIds.isEmpty()) {
            saveArticleTags(article.getId(), tagIds);
        }

        return article;
    }

    @Override
    @Transactional
    public Article updateArticle(Article article, List<Long> tagIds) {
        Article existingArticle = articleMapper.selectById(article.getId());
        if (existingArticle == null) {
            throw new BizException(ResponseCode.NOT_FOUND.getCode(), "文章不存在");
        }

        existingArticle.setArticleTitle(article.getArticleTitle());
        existingArticle.setArticleCover(article.getArticleCover());
        existingArticle.setCategoryId(article.getCategoryId());
        existingArticle.setArticleSummary(article.getArticleSummary());
        existingArticle.setMdFilePath(article.getMdFilePath());
        existingArticle.setIsTop(article.getIsTop());
        existingArticle.setStatus(article.getStatus());

        articleMapper.updateById(existingArticle);

        LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleTag::getArticleId, article.getId());
        articleTagMapper.delete(wrapper);

        if (tagIds != null && !tagIds.isEmpty()) {
            saveArticleTags(article.getId(), tagIds);
        }

        return existingArticle;
    }

    @Override
    @Transactional
    public void deleteArticle(Long articleId) {
        articleMapper.deleteById(articleId);

        LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleTag::getArticleId, articleId);
        articleTagMapper.delete(wrapper);
    }

    @Override
    public Article getArticleDetail(Long articleId) {
        Article article = articleMapper.selectById(articleId);
        if (article == null) {
            throw new BizException(ResponseCode.NOT_FOUND.getCode(), "文章不存在");
        }
        return article;
    }

    @Override
    public Page<Article> getArticleList(Integer pageNum, Integer pageSize, Long categoryId, String keyword) {
        Page<Article> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getStatus, 1);
        wrapper.eq(Article::getIsDeleted, 0);

        if (categoryId != null) {
            wrapper.eq(Article::getCategoryId, categoryId);
        }
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(Article::getArticleTitle, keyword);
        }
        wrapper.orderByDesc(Article::getIsTop).orderByDesc(Article::getCreateTime);

        return articleMapper.selectPage(page, wrapper);
    }

    @Override
    public void incrementViewCount(Long articleId) {
        redisTemplate.opsForValue().increment("article:view:" + articleId);
    }

    private void saveArticleTags(Long articleId, List<Long> tagIds) {
        for (Long tagId : tagIds) {
            ArticleTag articleTag = new ArticleTag();
            articleTag.setArticleId(articleId);
            articleTag.setTagId(tagId);
            articleTagMapper.insert(articleTag);
        }
    }
}
