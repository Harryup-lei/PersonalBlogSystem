# 个人博客系统后端

基于 Spring Boot + MySQL + Redis + MyBatis-Plus 的个人博客系统后端 API。

## 技术栈

| 技术 | 说明 |
|------|------|
| Spring Boot 3.2.0 | Web框架 |
| MyBatis-Plus 3.5.5 | ORM框架 |
| MySQL 8.0 | 关系数据库 |
| Redis | 缓存数据库 |
| JWT | 身份认证 |
| Hutool | 工具类库 |

## 项目结构

```
backend/
├── src/main/java/com/blog/
│   ├── BlogApplication.java          # 应用启动类
│   ├── common/                        # 公共类
│   │   ├── Result.java                # 统一响应结果
│   │   ├── BizException.java          # 业务异常
│   │   └── ResponseCode.java          # 响应码枚举
│   ├── config/                        # 配置类
│   │   ├── MybatisPlusConfig.java     # MyBatis-Plus配置
│   │   ├── RedisConfig.java           # Redis配置
│   │   ├── WebMvcConfig.java          # Web MVC配置
│   │   └── GlobalExceptionHandler.java # 全局异常处理
│   ├── controller/                    # 控制层
│   │   ├── AuthController.java        # 认证接口
│   │   ├── UserController.java        # 用户接口
│   │   ├── ArticleController.java    # 文章接口
│   │   ├── CategoryController.java   # 分类接口
│   │   ├── TagController.java         # 标签接口
│   │   └── CommentController.java     # 评论接口
│   ├── service/                       # 服务层
│   │   ├── impl/                     # 服务实现
│   │   └── UserService.java
│   │   └── ...
│   ├── mapper/                        # 持久层
│   │   └── *Mapper.java
│   ├── entity/                        # 实体类
│   │   └── *.java
│   └── util/                          # 工具类
│       └── JwtUtil.java               # JWT工具类
├── src/main/resources/
│   ├── application.yml                # 主配置文件
│   ├── application-dev.yml           # 开发环境配置
│   └── application-prod.yml           # 生产环境配置
├── docs/sql/
│   └── init.sql                       # 数据库初始化脚本
└── pom.xml                           # Maven配置文件
```

## 快速开始

### 1. 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis

### 2. 数据库配置

1. 创建数据库并导入初始化脚本：

```bash
mysql -u root -p < docs/sql/init.sql
```

2. 修改 `src/main/resources/application.yml` 中的数据库连接配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/personal_blog
    username: root
    password: 123456
```

### 3. 编译运行

```bash
# 编译项目
mvn clean package

# 运行项目
mvn spring-boot:run

# 或直接运行jar包
java -jar target/personal-blog-system-1.0.0.jar
```

项目启动后访问：`http://localhost:8080/api`

## API 接口

### 认证接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /auth/register | 用户注册 |
| POST | /auth/login | 用户登录 |

### 用户接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /user/info | 获取用户信息 |
| PUT | /user/info | 更新用户信息 |

### 文章接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /article/publish | 发布文章 |
| PUT | /article/update | 更新文章 |
| DELETE | /article/{id} | 删除文章 |
| GET | /article/{id} | 获取文章详情 |
| GET | /article/list | 获取文章列表 |

### 分类接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /category/create | 创建分类 |
| PUT | /category/update | 更新分类 |
| DELETE | /category/{id} | 删除分类 |
| GET | /category/all | 获取所有分类 |
| GET | /category/list | 获取分类列表 |

### 标签接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /tag/create | 创建标签 |
| PUT | /tag/update | 更新标签 |
| DELETE | /tag/{id} | 删除标签 |
| GET | /tag/all | 获取所有标签 |
| GET | /tag/list | 获取标签列表 |

### 评论接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /comment/add | 添加评论 |
| PUT | /comment/update | 更新评论 |
| DELETE | /comment/{id} | 删除评论 |
| GET | /comment/article/{articleId} | 获取文章评论列表 |
| GET | /comment/article/{articleId}/all | 获取文章所有评论 |

## 数据库表结构

| 表名 | 说明 |
|------|------|
| user | 用户表 |
| blog_category | 分类表 |
| blog_tag | 标签表 |
| blog_article | 文章表 |
| blog_article_tag | 文章标签关联表 |
| blog_comment | 评论表 |
| blog_config | 系统配置表 |

## 默认测试账号

- 用户名: admin
- 密码: 123456

## License

MIT
