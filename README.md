# 个人博客系统（Personal Blog System）

一个基于 Spring Boot 3 + React 18 构建的全栈个人博客系统，支持文章的发布、管理、分类、标签、评论等功能。

## 项目概述

本博客系统采用经典的前后端分离架构，后端提供 RESTful API，前端使用 React 构建单页应用。系统支持 Markdown 文章编写、文章分类与标签管理、用户评论等功能。

## 技术栈

### 后端技术
- **框架**: Spring Boot 3.2.0
- **ORM**: MyBatis Plus 3.5.5
- **数据库**: MySQL 8.0
- **缓存**: Redis
- **认证**: JWT (JJWT 0.12.3)
- **工具库**: Hutool 5.8.23、Lombok、Apache Commons Lang3
- **构建工具**: Maven

### 前端技术
- **框架**: React 18.2.0 + TypeScript
- **路由**: React Router DOM 6.21.1
- **构建工具**: Vite 5.0.10
- **HTTP 客户端**: Axios 1.6.2
- **UI 组件**: 自定义组件 + CSS
- **日期处理**: Day.js 1.11.10
- **图标**: Lucide React 0.303.0
- **Markdown 渲染**: React Markdown 9.0.1 + Remark GFM 4.0.1
- **代码高亮**: React Syntax Highlighter 15.5.0

## 项目结构

```
PersonalBlogSystem/
├── backend/                    # 后端模块（Spring Boot）
│   ├── src/
│   │   └── main/
│   │       ├── java/com/blog/
│   │       │   ├── BlogApplication.java      # 主启动类
│   │       │   ├── config/                   # 配置类
│   │       │   │   ├── GlobalExceptionHandler.java  # 全局异常处理
│   │       │   │   ├── MybatisPlusConfig.java        # MyBatis Plus 配置
│   │       │   │   ├── RedisConfig.java              # Redis 配置
│   │       │   │   └── WebMvcConfig.java             # Web MVC 配置
│   │       │   ├── common/                    # 通用类
│   │       │   │   ├── Result.java           # 统一返回结果
│   │       │   │   ├── ResponseCode.java     # 响应码枚举
│   │       │   │   └── BizException.java     # 业务异常类
│   │       │   ├── controller/               # 控制器层
│   │       │   │   ├── ArticleController.java
│   │       │   │   ├── CategoryController.java
│   │       │   │   ├── CommentController.java
│   │       │   │   ├── FileController.java
│   │       │   │   ├── TagController.java
│   │       │   │   └── UserController.java
│   │       │   ├── entity/                   # 实体类
│   │       │   │   ├── Article.java
│   │       │   │   ├── Category.java
│   │       │   │   ├── Comment.java
│   │       │   │   ├── Tag.java
│   │       │   │   └── User.java
│   │       │   ├── mapper/                   # MyBatis Mapper 接口
│   │       │   ├── service/                  # 业务逻辑层
│   │       │   │   ├── ArticleService.java
│   │       │   │   ├── CategoryService.java
│   │       │   │   ├── CommentService.java
│   │       │   │   ├── TagService.java
│   │       │   │   └── UserService.java
│   │       │   └── util/                     # 工具类
│   │       │       └── JwtUtil.java          # JWT 工具类
│   │       └── resources/
│   │           ├── application.yml           # 主配置文件
│   │           ├── application-dev.yml       # 开发环境���置
│   │           ├── application-prod.yml      # 生产环境配置
│   │           └── mapper/                   # MyBatis XML 映射文件
│   └── pom.xml                               # Maven 依赖配置
│
├── frontend/                    # 前端模块（React + TypeScript）
│   ├── src/
│   │   ├── components/                       # 通用组件
│   │   │   ├── Layout.tsx                    # 布局组件
│   │   │   ├── Layout.css
│   │   │   ├── Button.tsx
│   │   │   ├── Button.css
│   │   │   └── ProtectedRoute.tsx            # 路由守卫组件
│   │   ├── context/                          # React Context
│   │   │   └── AuthContext.tsx               # 认证上下文
│   │   ├── pages/                            # 页面组件
│   │   │   ├── Home.tsx                      # 首页
│   │   │   ├── Home.css
│   │   │   ├── Post.tsx                      # 文章详情页
│   │   │   ├── Post.css
│   │   │   ├── Login.tsx                     # 登录页
│   │   │   ├── Register.tsx                  # 注册页
│   │   │   ├── Auth.css
│   │   │   ├── Write.tsx                     # 写文章页
│   │   │   ├── Write.css
│   │   │   └── Profile.tsx                   # 个人中心页
│   │   ├── types/                            # TypeScript 类型定义
│   │   │   └── index.ts
│   │   ├── utils/                            # 工具函数
│   │   │   ├── request.ts                    # Axios 封装
│   │   │   └── api.ts                        # API 接口定义
│   │   ├── App.tsx
│   │   ├── main.tsx
│   │   └── index.css
│   ├── index.html
│   ├── vite.config.ts
│   ├── tsconfig.json
│   ├── package.json
│   └── package-lock.json
│
├── file/                                 # 文件存储目录（上传的图片等）
├── docs/                                 # 项目文档
│   └── sql/
│       └── init.sql                      # 数据库初始化脚本
├── .gitignore
└── README.md                             # 项目说明文档（本文件）
```

## 核心功能

### 1. 用户认证
- 用户注册与登录
- JWT Token 认证机制
- 路由守卫（未登录用户无法访问需要权限的页面）
- Token 过期自动处理

### 2. 文章管理
- 文章的创建、编辑、发布、删除
- 支持 Markdown 语法编写
- 文章分类管理
- 文章标签管理
- 文章浏览与列表展示

### 3. 评论系统
- 文章评论功能
- 评论管理（删除、审核）

### 4. 文件上传
- 支持图片上传
- 文件存储本地文件系统
- 文件访问路径配置

## 快速开始

### 环境要求

| 环境 | 版本要求 |
|------|---------|
| JDK | 17 或更高 |
| Node.js | 18+ |
| MySQL | 8.0+ |
| Redis | 6.0+ |
| Maven | 3.8+ |

### 后端启动

1. **配置数据库**
   - 创建数据库：`personal_blog`
   - 导入初始化脚本：`backend/docs/sql/init.sql`

2. **修改配置文件**
   - 编辑 `backend/src/main/resources/application.yml`：
     - 修改数据库连接信息（用户名、密码）
     - 根据需要修改 Redis 配置
     - 修改 JWT 密钥（生产环境必须修改）

3. **启动应用**
   ```bash
   # 进入后端目录
   cd backend

   # 使用 Maven 运行
   mvn spring-boot:run

   # 或打包后运行
   mvn clean package
   java -jar target/personal-blog-system-1.0.0.jar
   ```

4. **验证启动**
   - 访问 API 文档（如有）：http://localhost:8080/api
   - 后端服务端口：8080
   - 接口前��：/api

### 前端启动

1. **安装依赖**
   ```bash
   cd frontend
   npm install
   ```

2. **启动开发服务器**
   ```bash
   npm run dev
   ```

3. **访问应用**
   - 前端开发服务器：http://localhost:5173（Vite 默认端口）
   - 生产环境构建：
     ```bash
     npm run build
     npm run preview
     ```

## API 接口说明

### 用户相关
- `POST /api/user/register` - 用户注册
- `POST /api/user/login` - 用户登录
- `GET /api/user/info` - 获取用户信息（需认证）
- `PUT /api/user/info` - 更新用户信息（需认证）

### 文章相关
- `GET /api/article/list` - 文章列表（分页）
- `GET /api/article/{id}` - 文章详情
- `POST /api/article` - 创建文章（需认证）
- `PUT /api/article` - 更新文章（需认证）
- `DELETE /api/article/{id}` - 删除文章（需认证）

### 分类与标签
- `GET /api/category/list` - 分类列表
- `GET /api/tag/list` - 标签列表

### 评论相关
- `GET /api/comment/list` - 评论列表
- `POST /api/comment` - 发表评论
- `DELETE /api/comment/{id}` - 删除评论（需认证）

### 文件上传
- `POST /api/file/upload` - 文件上传（需认证）

## 配置说明

### JWT 配置
在 `application.yml` 中：
```yaml
jwt:
  secret: blog-jwt-secret-key-2024-do-not-share-this-secret  # JWT 密钥（生产环境必须修改）
  expiration: 86400000                                      # Token 过期时间（毫秒，默认 24 小时）
  header: Authorization                                     # 请求头名称
  prefix: "Bearer "                                         # Token 前缀
```

### 文件上传配置
```yaml
upload:
  path: d:/code/AiCode/PersonalBlogSystem/file  # 文件存储路径
  base-url: http://localhost:8080/file          # 文件访问基础 URL
```

### 数据库配置
```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/personal_blog?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root
    password: your_password  # 修改为实际密码
```

## 数据库表结构

主要数据表包括：
- `user` - 用户表
- `article` - 文章表
- `category` - 分类表
- `tag` - 标签表
- `article_tag` - 文章-标签关联表
- `comment` - 评论表

详细的表结构请参考 `backend/docs/sql/init.sql`

## 前端页面说明

| 路径 | 页面 | 说明 |
|------|------|------|
| `/` | 首页 | 文章列表展示 |
| `/post/:id` | 文章详情 | 查看文章内容及评论 |
| `/login` | 登录页 | 用户登录 |
| `/register` | 注册页 | 用户注册 |
| `/write` | 写文章 | 创建或编辑文章（需登录） |
| `/profile` | 个人中心 | 个人信息管理（需登录） |

## 注意事项

1. **安全提醒**
   - 生产环境务必修改 `jwt.secret` 为强密码
   - 数据库密码建议使用环境变量或配置中心管理
   - 启用 HTTPS（Nginx 反向代理配置）

2. **性能优化建议**
   - 启用 Redis 缓存热点数据
   - 图片等静态资源使用 CDN
   - 数据库查询添加索引优化

3. **开发建议**
   - 后端接口返回统一使用 `Result<T>` 封装
   - 前端所有 API 请求通过 `request.ts` 封装
   - 新增接口请更新 `frontend/src/utils/api.ts`

## 常见问题

### 1. JWT 解析异常：Invalid compact JWT string
**原因**：前端未正确传递 Token 或 Token 格式错误。

**解决方案**：
- 检查前端 `request.ts` 中的 Token 获取逻辑
- 确认登录后 Token 已正确存储到 localStorage
- 验证请求头中的 Authorization 格式为 `Bearer {token}`

### 2. 文件上传失败
**原因**：上传目录不存在或没有写入权限。

**解决方案**：
- 确保 `upload.path` 配置的目录存在
- 给予该目录写权限
- 检查文件大小是否超过限制（默认 10MB）

### 3. 前端跨域问题
开发环境下，前端运行在 5173 端口，后端在 8080 端口，Vite 已配置代理。若出现跨域���误，请检查 `vite.config.ts` 配置。

## 扩展功能建议

- [ ] 文章搜索功能（Elasticsearch）
- [ ] 文章点赞/收藏
- [ ] 文章访问统计
- [ ] 邮件通知（评论回复、系统消息）
- [ ] 第三方登录（GitHub、Gitee 等）
- [ ] 文章置顶功能
- [ ] 文章推荐系统
- [ ] 后台管理界面

## 许可证

本项目仅供学习交流使用。

## 联系方式

如有问题或建议，欢迎提交 Issue 或 Pull Request。

---

**开发时间**: 2024
**版本**: 1.0.0
