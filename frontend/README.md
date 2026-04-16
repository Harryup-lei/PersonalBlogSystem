# 个人博客系统前端

基于 React + TypeScript + Vite 的个人博客系统前端应用。

## 技术栈

| 技术 | 说明 |
|------|------|
| React 18 | UI框架 |
| TypeScript | 类型安全 |
| Vite | 构建工具 |
| React Router 6 | 路由管理 |
| Axios | HTTP请求 |
| React Markdown | Markdown渲染 |
| Lucide React | 图标库 |
| Day.js | 日期处理 |

## 项目结构

```
frontend/
├── src/
│   ├── components/           # 组件
│   │   ├── Layout.tsx       # 布局组件
│   │   ├── Button.tsx       # 按钮组件
│   │   └── ProtectedRoute.tsx # 路由保护
│   ├── context/              # React Context
│   │   └── AuthContext.tsx   # 认证上下文
│   ├── pages/                # 页面
│   │   ├── Home.tsx          # 首页
│   │   ├── Login.tsx         # 登录页
│   │   ├── Register.tsx      # 注册页
│   │   ├── Post.tsx          # 文章详情页
│   │   ├── Write.tsx         # 写文章页
│   │   └── Profile.tsx       # 个人中心
│   ├── utils/                # 工具
│   │   ├── request.ts        # axios封装
│   │   └── api.ts            # API接口
│   ├── types/                # 类型定义
│   │   └── index.ts
│   ├── App.tsx               # 应用入口
│   └── main.tsx              # 入口文件
├── index.html
├── package.json
├── tsconfig.json
├── vite.config.ts
└── README.md
```

## 快速开始

### 安装依赖

```bash
cd frontend
npm install
```

### 开发模式

```bash
npm run dev
```

项目启动后访问：`http://localhost:3000`

### 构建生产版本

```bash
npm run build
```

## 页面路由

| 路径 | 页面 | 说明 |
|------|------|------|
| / | Home | 首页（文章列表） |
| /post/:id | Post | 文章详情 |
| /write | Write | 写文章（需登录） |
| /profile | Profile | 个人中心（需登录） |
| /login | Login | 登录 |
| /register | Register | 注册 |

## 功能特性

### 用户认证
- JWT Token 认证
- Token 存储在 localStorage
- 自动请求拦截添加 token
- 路由守卫保护需要登录的页面

### 首页
- 文章列表展示（标题、封面、分类、时间、阅读数）
- 分页功能
- 分类筛选
- 关键词搜索

### 文章详情
- Markdown 内容渲染
- 代码高亮
- 评论功能
- 作者操作按钮

### 写文章
- Markdown 编辑器
- 左侧编辑、右侧预览
- 分类和标签选择
- 封面图片上传
- 草稿保存

### 个人中心
- 用户信息编辑
- 头像上传
- 简介编辑

## 与后端对接

前端通过 Vite 代理将 `/api` 请求转发到后端 `http://localhost:8080/api`。

如需修改后端地址，编辑 `vite.config.ts` 中的 proxy 配置。