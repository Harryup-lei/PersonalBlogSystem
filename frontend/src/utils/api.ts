import request from './request';
import type { Article, Category, Tag, Comment, PageResult, ApiResponse } from '@/types';

export const articleApi = {
  getArticleList: (params: {
    pageNum: number;
    pageSize: number;
    categoryId?: number;
    keyword?: string;
  }) => {
    return request.get<any, ApiResponse<PageResult<Article>>>('/article/list', { params });
  },

  getArticleDetail: (id: number) => {
    return request.get<any, ApiResponse<Article>>(`/article/${id}`);
  },

  publishArticle: (data: {
    articleTitle: string;
    articleCover?: string;
    categoryId: number;
    articleSummary?: string;
    mdFilePath?: string;
    isTop?: number;
    status?: number;
    tagIds?: number[];
  }) => {
    return request.post<any, ApiResponse<Article>>('/article/publish', data);
  },

  updateArticle: (data: {
    id: number;
    articleTitle: string;
    articleCover?: string;
    categoryId: number;
    articleSummary?: string;
    mdFilePath?: string;
    isTop?: number;
    status?: number;
    tagIds?: number[];
  }) => {
    return request.put<any, ApiResponse<Article>>('/article/update', data);
  },

  deleteArticle: (id: number) => {
    return request.delete<any, ApiResponse<void>>(`/article/${id}`);
  },
};

export const categoryApi = {
  getAllCategories: () => {
    return request.get<any, ApiResponse<Category[]>>('/category/all');
  },

  getCategoryList: (params: { pageNum: number; pageSize: number }) => {
    return request.get<any, ApiResponse<PageResult<Category>>>('/category/list', { params });
  },
};

export const tagApi = {
  getAllTags: () => {
    return request.get<any, ApiResponse<Tag[]>>('/tag/all');
  },

  getTagList: (params: { pageNum: number; pageSize: number }) => {
    return request.get<any, ApiResponse<PageResult<Tag>>>('/tag/list', { params });
  },
};

export const commentApi = {
  getArticleComments: (articleId: number, params: { pageNum: number; pageSize: number }) => {
    return request.get<any, ApiResponse<PageResult<Comment>>>(`/comment/article/${articleId}`, { params });
  },

  addComment: (data: { articleId: number; commentator: string; commentEmail?: string; commentContent: string }) => {
    return request.post<any, ApiResponse<Comment>>('/comment/add', data);
  },

  deleteComment: (id: number) => {
    return request.delete<any, ApiResponse<void>>(`/comment/${id}`);
  },
};

export const authApi = {
  login: (data: { username: string; password: string }) => {
    return request.post<any, ApiResponse<{ token: string }>>('/auth/login', data);
  },

  register: (data: { username: string; password: string; email?: string }) => {
    return request.post<any, ApiResponse<void>>('/auth/register', data);
  },

  logout: () => {
    return request.post<any, ApiResponse<void>>('/auth/logout');
  },

  refreshToken: () => {
    return request.post<any, ApiResponse<{ token: string }>>('/auth/refresh');
  },
};

export const userApi = {
  getUserInfo: () => {
    return request.get<any, ApiResponse<any>>('/user/info');
  },

  updateUserInfo: (data: { nickname?: string; avatar?: string; description?: string }) => {
    return request.put<any, ApiResponse<any>>('/user/info', data);
  },

  changePassword: (data: { oldPassword: string; newPassword: string }) => {
    return request.post<any, ApiResponse<void>>('/user/password', data);
  },
};

export const fileApi = {
  uploadFile: (file: File) => {
    const formData = new FormData();
    formData.append('file', file);
    return request.post<any, ApiResponse<{ path: string; url: string; originalName: string }>>('/file/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  },

  uploadImage: (file: File) => {
    const formData = new FormData();
    formData.append('file', file);
    return request.post<any, ApiResponse<{ url: string; originalName: string }>>('/file/upload/image', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  },

  saveMarkdown: (data: { content: string; filename?: string }) => {
    return request.post<any, ApiResponse<{ path: string; url: string }>>('/file/save-markdown', data);
  },
};