import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { articleApi, categoryApi } from '@/utils/api';
import { Category } from '@/types';
import { Search, Calendar, Eye, ChevronLeft, ChevronRight } from 'lucide-react';
import dayjs from 'dayjs';
import './Home.css';

export function Home() {
  const [articles, setArticles] = useState<any[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(1);
  const [total, setTotal] = useState(0);
  const [selectedCategory, setSelectedCategory] = useState<number | null>(null);
  const [keyword, setKeyword] = useState('');
  const [searchKeyword, setSearchKeyword] = useState('');
  const pageSize = 10;

  useEffect(() => {
    fetchCategories();
  }, []);

  useEffect(() => {
    fetchArticles();
  }, [page, selectedCategory, searchKeyword]);

  const fetchCategories = async () => {
    try {
      const res = await categoryApi.getAllCategories();
      setCategories(res.data || []);
    } catch (err) {
      console.error('获取分类失败', err);
    }
  };

  const fetchArticles = async () => {
    setLoading(true);
    try {
      const res = await articleApi.getArticleList({
        pageNum: page,
        pageSize,
        categoryId: selectedCategory || undefined,
        keyword: searchKeyword || undefined,
      });
      setArticles(res.data?.records || []);
      setTotal(res.data?.total || 0);
    } catch (err) {
      console.error('获取文章列表失败', err);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    setPage(1);
    setSearchKeyword(keyword);
  };

  const handleCategoryClick = (categoryId: number | null) => {
    setSelectedCategory(categoryId);
    setPage(1);
  };

  const totalPages = Math.ceil(total / pageSize);

  return (
    <div className="home">
      <div className="container">
        <div className="search-bar">
          <form onSubmit={handleSearch}>
            <div className="search-input-wrapper">
              <Search size={20} className="search-icon" />
              <input
                type="text"
                placeholder="搜索文章..."
                value={keyword}
                onChange={(e) => setKeyword(e.target.value)}
                className="search-input"
              />
            </div>
          </form>
        </div>

        <div className="category-filter">
          <button
            className={`category-tag ${selectedCategory === null ? 'active' : ''}`}
            onClick={() => handleCategoryClick(null)}
          >
            全部
          </button>
          {categories.map((category) => (
            <button
              key={category.id}
              className={`category-tag ${selectedCategory === category.id ? 'active' : ''}`}
              onClick={() => handleCategoryClick(category.id)}
            >
              {category.categoryName}
            </button>
          ))}
        </div>

        {loading ? (
          <div className="loading">加载中...</div>
        ) : articles.length === 0 ? (
          <div className="empty">暂无文章</div>
        ) : (
          <>
            <div className="article-list">
              {articles.map((article) => (
                <Link
                  to={`/post/${article.id}`}
                  key={article.id}
                  className="article-card"
                >
                  {article.articleCover && (
                    <div className="article-cover">
                      <img src={article.articleCover} alt={article.articleTitle} />
                    </div>
                  )}
                  <div className="article-content">
                    <h3 className="article-title">
                      {article.isTop === 1 && <span className="top-badge">置顶</span>}
                      {article.articleTitle}
                    </h3>
                    {article.articleSummary && (
                      <p className="article-summary">{article.articleSummary}</p>
                    )}
                    <div className="article-meta">
                      <span className="meta-item">
                        <Calendar size={14} />
                        {dayjs(article.createTime).format('YYYY-MM-DD')}
                      </span>
                      <span className="meta-item">
                        <Eye size={14} />
                        {article.viewCount} 阅读
                      </span>
                    </div>
                  </div>
                </Link>
              ))}
            </div>

            {totalPages > 1 && (
              <div className="pagination">
                <button
                  className="page-btn"
                  disabled={page === 1}
                  onClick={() => setPage(page - 1)}
                >
                  <ChevronLeft size={20} />
                </button>
                <span className="page-info">
                  第 {page} / {totalPages} 页，共 {total} 篇文章
                </span>
                <button
                  className="page-btn"
                  disabled={page === totalPages}
                  onClick={() => setPage(page + 1)}
                >
                  <ChevronRight size={20} />
                </button>
              </div>
            )}
          </>
        )}
      </div>
    </div>
  );
}