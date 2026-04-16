-- 个人博客系统数据库初始化脚本
-- 数据库名称: personal_blog

-- 创建数据库
CREATE DATABASE IF NOT EXISTS personal_blog
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE personal_blog;

-- 1. 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户唯一ID，自增',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名（登录账号）',
    `password` VARCHAR(100) NOT NULL COMMENT '登录密码（加密存储）',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '用户昵称',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '用户头像地址',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '联系邮箱',
    `description` VARCHAR(250) DEFAULT NULL COMMENT '自我简介',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '用户状态（1-正常，0-禁用）',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标识',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 2. 文章分类表
CREATE TABLE IF NOT EXISTS `blog_category` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '分类唯一ID，自增',
    `category_name` VARCHAR(50) NOT NULL COMMENT '分类名称',
    `category_alias` VARCHAR(50) DEFAULT NULL COMMENT '分类别名（URL路径）',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '分类描述',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标识',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章分类表';

-- 3. 文章标签表
CREATE TABLE IF NOT EXISTS `blog_tag` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '标签唯一ID，自增',
    `tag_name` VARCHAR(50) NOT NULL COMMENT '标签名称',
    `tag_alias` VARCHAR(50) DEFAULT NULL COMMENT '标签别名（URL路径）',
    `sort` INT NOT NULL DEFAULT 0 COMMENT '标签排序值',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标识',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_tag_name` (`tag_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章标签表';

-- 4. 文章表
CREATE TABLE IF NOT EXISTS `blog_article` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '文章唯一ID，自增',
    `article_title` VARCHAR(255) NOT NULL COMMENT '文章标题',
    `article_cover` VARCHAR(255) DEFAULT NULL COMMENT '文章封面图片地址',
    `category_id` BIGINT NOT NULL COMMENT '所属分类ID',
    `article_summary` VARCHAR(500) DEFAULT NULL COMMENT '文章摘要',
    `md_file_path` VARCHAR(550) DEFAULT NULL COMMENT '文章Markdown文件的相对路径或绝对路径',
    `is_top` TINYINT NOT NULL DEFAULT 0 COMMENT '是否置顶（1-是，0-否）',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '文章状态（1-已发布，0-草稿，2-待审核）',
    `view_count` BIGINT NOT NULL DEFAULT 0 COMMENT '浏览次数',
    `like_count` BIGINT NOT NULL DEFAULT 0 COMMENT '点赞次数',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标识',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_category_id` (`category_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_is_top` (`is_top`),
    INDEX `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章表';

-- 5. 文章-标签关联表
CREATE TABLE IF NOT EXISTS `blog_article_tag` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '关联ID，自增',
    `article_id` BIGINT NOT NULL COMMENT '文章ID',
    `tag_id` BIGINT NOT NULL COMMENT '标签ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_article_tag` (`article_id`, `tag_id`),
    INDEX `idx_article_id` (`article_id`),
    INDEX `idx_tag_id` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章-标签关联表';

-- 6. 文章评论表
CREATE TABLE IF NOT EXISTS `blog_comment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '评论唯一ID，自增',
    `article_id` BIGINT NOT NULL COMMENT '所属文章ID',
    `commentator` VARCHAR(50) NOT NULL COMMENT '评论人昵称',
    `comment_email` VARCHAR(100) DEFAULT NULL COMMENT '评论人邮箱',
    `comment_content` VARCHAR(500) NOT NULL COMMENT '评论内容',
    `like_count` BIGINT NOT NULL DEFAULT 0 COMMENT '评论点赞数',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '评论状态（1-已通过，0-待审核，2-已拒绝）',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标识',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_article_id` (`article_id`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章评论表';

-- 7. 系统配置表
CREATE TABLE IF NOT EXISTS `blog_config` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '配置ID，自增',
    `config_key` VARCHAR(100) NOT NULL COMMENT '配置键名',
    `config_value` TEXT DEFAULT NULL COMMENT '配置值',
    `config_desc` VARCHAR(255) DEFAULT NULL COMMENT '配置说明',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- 初始化测试数据
INSERT INTO `user` (`username`, `password`, `nickname`, `email`, `description`, `status`) VALUES
('admin', 'e10adc3949ba59abbe56e057f20f883e', '博主', 'admin@example.com', '这是我的个人博客', 1);

INSERT INTO `blog_category` (`category_name`, `category_alias`, `description`) VALUES
('技术', 'tech', '技术相关文章'),
('生活', 'life', '生活随笔'),
('随笔', 'essay', '随笔感想');

INSERT INTO `blog_tag` (`tag_name`, `tag_alias`, `sort`) VALUES
('Java', 'java', 1),
('SpringBoot', 'springboot', 2),
('MySQL', 'mysql', 3),
('Redis', 'redis', 4),
('前端', 'frontend', 5);
