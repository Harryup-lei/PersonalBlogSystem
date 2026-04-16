# 个人博客系统 databases
个人博客系统数据库设计文档
 
一、文档概述
 
1.1 文档目的
 
本文档用于定义个人博客系统的数据库结构、表关系、字段属性、索引及约束，为系统开发、测试、维护提供统一的数据规范，确保数据设计的合理性、可扩展性与稳定性。
 
1.2 数据库选型
 
采用MySQL 8.0关系型数据库，具备轻量、稳定、易维护的特点，适配个人博客系统的业务需求。
 
1.3 数据库命名规范
 
- 数据库名： blog_system
- 表名：小写字母+下划线，采用业务语义化命名，如 user 、 blog_article 
- 字段名：小写字母+下划线，禁止使用关键字，字段含义清晰
- 主键：统一使用 id （自增BIGINT），保证唯一性
- 时间字段：统一使用 datetime 类型，命名为 create_time 、 update_time 、 delete_time 
- 逻辑删除：采用 is_deleted 字段（0-未删除，1-已删除）
 
1.4 核心业务模块
 
- 用户管理模块
- 文章管理模块
- 分类管理模块
- 标签管理模块
- 评论管理模块
- 系统配置模块
 
二、数据库整体结构
 
数据库名称： personal_blog 
字符集： utf8mb4 （支持emoji表情）
排序规则： utf8mb4_unicode_ci 
存储引擎： InnoDB （支持事务、外键关联）
 
三、数据表详细设计
 
3.1 用户表（user）
 
表说明：存储博客系统管理员/个人用户信息，单用户模式为主
 
字段名 数据类型 长度 是否为空 主键 默认值 字段说明 
id BIGINT - NO YES - 用户唯一ID，自增 
username VARCHAR 50 NO NO - 用户名（登录账号） 
password VARCHAR 100 NO NO - 登录密码（加密存储） 
nickname VARCHAR 50 YES NO NULL 用户昵称 
avatar VARCHAR 255 YES NO NULL 用户头像地址 
email VARCHAR 100 YES NO NULL 联系邮箱 
description VARCHAR 250 YES NO NULL 自我简介 
status TINYINT - NO NO 1 用户状态（1-正常，0-禁用） 
is_deleted TINYINT - NO NO 0 逻辑删除标识 
create_time DATETIME - NO NO CURRENT_TIMESTAMP 创建时间 
update_time DATETIME - NO NO CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP 更新时间 
 
索引设计：
 
- 主键索引： PRIMARY KEY (id) 
- 唯一索引： UNIQUE KEY idx_username (username) 
 
3.2 文章分类表（blog_category）
 
表说明：存储文章分类信息，支持多级分类（一级分类为主）
 
字段名 数据类型 长度 是否为空 主键 默认值 字段说明 
id BIGINT - NO YES - 分类唯一ID，自增 
category_name VARCHAR 50 NO NO - 分类名称 
category_alias VARCHAR 50 YES NO NULL 分类别名（URL路径）  
description VARCHAR 255 YES NO NULL 分类描述 
is_deleted TINYINT - NO NO 0 逻辑删除标识 
create_time DATETIME - NO NO CURRENT_TIMESTAMP 创建时间 
update_time DATETIME - NO NO CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP 更新时间 
 
索引设计：
 
- 主键索引： PRIMARY KEY (id) 
- 普通索引： INDEX idx_parent_id (parent_id) 
 
3.3 文章标签表（blog_tag）
 
表说明：存储文章标签信息，支持文章多标签关联
 
字段名 数据类型 长度 是否为空 主键 默认值 字段说明 
id BIGINT - NO YES - 标签唯一ID，自增 
tag_name VARCHAR 50 NO NO - 标签名称 
tag_alias VARCHAR 50 YES NO NULL 标签别名（URL路径） 
sort INT - NO NO 0 标签排序值 
is_deleted TINYINT - NO NO 0 逻辑删除标识 
create_time DATETIME - NO NO CURRENT_TIMESTAMP 创建时间 
update_time DATETIME - NO NO CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP 更新时间 
 
索引设计：
 
- 主键索引： PRIMARY KEY (id) 
- 唯一索引： UNIQUE KEY idx_tag_name (tag_name) 
 
3.4 文章表（blog_article）
 
表说明：存储博客文章核心内容
 
字段名 数据类型 长度 是否为空 主键 默认值 字段说明 
id BIGINT - NO YES - 文章唯一ID，自增 
article_title VARCHAR 255 NO NO - 文章标题 
article_cover VARCHAR 255 YES NO NULL 文章封面图片地址 
category_id BIGINT - NO NO - 所属分类ID 
article_summary VARCHAR 500 - YES NO NULL 文章摘要 
md_file_path VARCHAR 550 NO 文章的相对路径或者绝对路径
is_top TINYINT - NO NO 0 是否置顶（1-是，0-否） 
status TINYINT - NO NO 1 文章状态（1-已发布，0-草稿，2-待审核） 
view_count BIGINT - NO NO 0 浏览次数 
like_count BIGINT - NO NO 0 点赞次数 
is_deleted TINYINT - NO NO 0 逻辑删除标识 
create_time DATETIME - NO NO CURRENT_TIMESTAMP 创建时间 
update_time DATETIME - NO NO CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP 更新时间 
 
索引设计：
 
- 主键索引： PRIMARY KEY (id) 
- 普通索引： INDEX idx_category_id (category_id) 
- 普通索引： INDEX idx_status (status) 
- 普通索引： INDEX idx_is_top (is_top) 
 
3.5 文章-标签关联表（blog_article_tag）
 
表说明：实现文章与标签的多对多关联
 
字段名 数据类型 长度 是否为空 主键 默认值 字段说明 
id BIGINT - NO YES - 关联ID，自增 
article_id BIGINT - NO NO - 文章ID 
tag_id BIGINT - NO NO - 标签ID 
create_time DATETIME - NO NO CURRENT_TIMESTAMP 创建时间 
 
索引设计：
 
- 主键索引： PRIMARY KEY (id) 
- 联合索引： UNIQUE KEY idx_article_tag (article_id, tag_id) 
- 普通索引： INDEX idx_article_id (article_id) 
- 普通索引： INDEX idx_tag_id (tag_id) 
 
3.6 文章评论表（blog_comment）
 
表说明：存储文章评论及回复信息
 
字段名 数据类型 长度 是否为空 主键 默认值 字段说明 
id BIGINT - NO YES - 评论唯一ID，自增 
article_id BIGINT - NO NO - 所属文章ID 
commentator VARCHAR 50 NO NO - 评论人昵称 
comment_email VARCHAR 100 YES NO NULL 评论人邮箱 
comment_content VARCHAR 500 NO NO - 评论内容 
like_count BIGINT - NO NO 0 评论点赞数 
status TINYINT - NO NO 1 评论状态（1-已通过，0-待审核，2-已拒绝） 
is_deleted TINYINT - NO NO 0 逻辑删除标识 
create_time DATETIME - NO NO CURRENT_TIMESTAMP 创建时间 
update_time DATETIME - NO NO CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP 更新时间 
 
索引设计：
 
- 主键索引： PRIMARY KEY (id) 
- 普通索引： INDEX idx_article_id (article_id) 
- 普通索引： INDEX idx_parent_id (parent_id) 
- 普通索引： INDEX idx_status (status) 
 
3.7 系统配置表（blog_config）
 
表说明：存储博客系统全局配置信息
 
字段名 数据类型 长度 是否为空 主键 默认值 字段说明 
id BIGINT - NO YES - 配置ID，自增 
config_key VARCHAR 100 NO NO - 配置键名 
config_value TEXT - YES NO NULL 配置值 
config_desc VARCHAR 255 YES NO NULL 配置说明 
create_time DATETIME - NO NO CURRENT_TIMESTAMP 创建时间 
update_time DATETIME - NO NO CURRENT_TIMESTAMP 更新时间

1.文章表和分类表，多对一，一篇文章对应归属一个分类，一个分类下有多篇文章
2.文章表和标签表，多对多
3.文章表和评论表，一对多
4.评论表：只采用一级评论即可





1请创建一个SpringBoot + MySQL + Redis + MyBatis 的后端项目,初始化项目，安装maven等相关依赖，
2.创建基本的java Spring项目结构，控制层，服务层，持久层，
3.配置数据库连接，数据库采用localhost的数据库，密码为123456，
4.创建表结构，参考database.md中的表结构设计，读取@PRD.md 中的需求，看表结构是否满足需求，不能则按照合理的表结构进行设计，我给你的database.md是个参考。
