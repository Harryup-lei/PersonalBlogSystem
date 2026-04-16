export interface User {
  id: number;
  username: string;
  nickname?: string;
  avatar?: string;
  email?: string;
  description?: string;
  status: number;
  createTime: string;
  updateTime: string;
}

export interface Article {
  id: number;
  articleTitle: string;
  articleCover?: string;
  categoryId: number;
  articleSummary?: string;
  mdFilePath?: string;
  isTop: number;
  status: number;
  viewCount: number;
  likeCount: number;
  createTime: string;
  updateTime: string;
  categoryName?: string;
  tags?: Tag[];
}

export interface Category {
  id: number;
  categoryName: string;
  categoryAlias?: string;
  description?: string;
  createTime: string;
  updateTime: string;
}

export interface Tag {
  id: number;
  tagName: string;
  tagAlias?: string;
  sort: number;
  createTime: string;
  updateTime: string;
}

export interface Comment {
  id: number;
  articleId: number;
  commentator: string;
  commentEmail?: string;
  commentContent: string;
  likeCount: number;
  status: number;
  createTime: string;
  updateTime: string;
}

export interface PageResult<T> {
  records: T[];
  total: number;
  size: number;
  current: number;
  pages: number;
}

export interface ApiResponse<T> {
  code: number;
  message: string;
  data: T;
}
