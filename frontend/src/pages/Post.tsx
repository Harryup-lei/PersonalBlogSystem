import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import ReactMarkdown from 'react-markdown';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { vscDarkPlus } from 'react-syntax-highlighter/dist/esm/styles/prism';
import { articleApi, commentApi } from '@/utils/api';
import { Article, Comment } from '@/types';
import { useAuth } from '@/context/AuthContext';
import { Button } from '@/components/Button';
import { Calendar, Eye, Edit, Trash2, MessageSquare } from 'lucide-react';
import dayjs from 'dayjs';
import './Post.css';

export function Post() {
  const { id } = useParams<{ id: string }>();
  const [article, setArticle] = useState<Article | null>(null);
  const [content, setContent] = useState('');
  const [comments, setComments] = useState<Comment[]>([]);
  const [loading, setLoading] = useState(true);
  const [contentLoading, setContentLoading] = useState(false);
  const [newComment, setNewComment] = useState('');
  const [commentator, setCommentator] = useState('');
  const [commentEmail, setCommentEmail] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const { user, isAuthenticated } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (id) {
      fetchArticle();
      fetchComments();
    }
  }, [id]);

  const fetchArticle = async () => {
    try {
      const res = await articleApi.getArticleDetail(Number(id));
      const articleData = res.data;
      setArticle(articleData);

      // 加载 Markdown 文件内容
      if (articleData.mdFilePath) {
        setContentLoading(true);
        try {
          let fileUrl = articleData.mdFilePath;
          // mdFilePath 格式: markdown/2026/04/16/xxx.md
          // 拼接为 /api/file/markdown/... 通过 Vite 代理访问
          fileUrl = '/api/file/' + fileUrl.replace(/^\//, '');
          const response = await fetch(fileUrl);
          if (response.ok) {
            const text = await response.text();
            setContent(text);
          } else {
            setContent('*[无法加载文章内容]*');
          }
        } catch {
          setContent('*[无法加载文章内容]*');
        } finally {
          setContentLoading(false);
        }
      } else {
        setContent(articleData.articleSummary || '*[暂无内容]*');
      }
    } catch (err) {
      console.error('获取文章详情失败', err);
    } finally {
      setLoading(false);
    }
  };

  const fetchComments = async () => {
    try {
      const res = await commentApi.getArticleComments(Number(id), { pageNum: 1, pageSize: 100 });
      setComments(res.data?.records || []);
    } catch (err) {
      console.error('获取评论失败', err);
    }
  };

  const handleDelete = async () => {
    if (!confirm('确定要删除这篇文章吗？')) return;
    try {
      await articleApi.deleteArticle(Number(id));
      navigate('/');
    } catch (err) {
      console.error('删除失败', err);
      alert('删除失败');
    }
  };

  const handleAddComment = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!newComment || !commentator) {
      alert('请填写昵称和评论内容');
      return;
    }

    setSubmitting(true);
    try {
      await commentApi.addComment({
        articleId: Number(id),
        commentator,
        commentEmail,
        commentContent: newComment,
      });
      setNewComment('');
      fetchComments();
    } catch (err) {
      console.error('评论失败', err);
      alert('评论失败');
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) {
    return <div className="post-loading">加载中...</div>;
  }

  if (!article) {
    return <div className="post-not-found">文章不存在</div>;
  }

  return (
    <div className="post">
      <div className="container">
        <article className="article-detail">
          <header className="article-header">
            <h1 className="article-title">{article.articleTitle}</h1>
            <div className="article-meta">
              <span className="meta-item">
                <Calendar size={16} />
                {dayjs(article.createTime).format('YYYY-MM-DD HH:mm')}
              </span>
              <span className="meta-item">
                <Eye size={16} />
                {article.viewCount} 阅读
              </span>
              <span className="meta-item">
                <MessageSquare size={16} />
                {comments.length} 评论
              </span>
            </div>
            {article.articleCover && (
              <img
                src={article.articleCover}
                alt={article.articleTitle}
                className="article-cover"
              />
            )}
          </header>

          <div className="article-actions">
            {isAuthenticated && user?.id === 1 && (
              <>
                <Link to={`/write?id=${article.id}`}>
                  <Button variant="secondary" size="small">
                    <Edit size={16} />
                    编辑
                  </Button>
                </Link>
                <Button variant="danger" size="small" onClick={handleDelete}>
                  <Trash2 size={16} />
                  删除
                </Button>
              </>
            )}
          </div>

          <div className="article-body">
            {contentLoading ? (
              <div className="content-loading">内容加载中...</div>
            ) : (
              <ReactMarkdown
                components={{
                  code({ node, className, children, ...props }) {
                    const match = /language-(\w+)/.exec(className || '');
                    const inline = !match;
                    return !inline ? (
                      <SyntaxHighlighter
                        style={vscDarkPlus}
                        language={match[1]}
                        PreTag="div"
                      >
                        {String(children).replace(/\n$/, '')}
                      </SyntaxHighlighter>
                    ) : (
                      <code className={className} {...props}>
                        {children}
                      </code>
                    );
                  },
                }}
              >
                {content || '*[暂无内容]*'}
              </ReactMarkdown>
            )}
          </div>
        </article>

        <section className="comments-section">
          <h2 className="comments-title">评论 ({comments.length})</h2>

          {isAuthenticated && (
            <form onSubmit={handleAddComment} className="comment-form">
              <input
                type="text"
                placeholder="昵称"
                value={commentator}
                onChange={(e) => setCommentator(e.target.value)}
                className="comment-input"
              />
              <input
                type="email"
                placeholder="邮箱（选填）"
                value={commentEmail}
                onChange={(e) => setCommentEmail(e.target.value)}
                className="comment-input"
              />
              <textarea
                placeholder="写下你的评论..."
                value={newComment}
                onChange={(e) => setNewComment(e.target.value)}
                className="comment-textarea"
                rows={4}
              />
              <Button type="submit" loading={submitting}>
                发布评论
              </Button>
            </form>
          )}

          <div className="comments-list">
            {comments.map((comment) => (
              <div key={comment.id} className="comment-item">
                <div className="comment-header">
                  <span className="commentator">{comment.commentator}</span>
                  <span className="comment-time">
                    {dayjs(comment.createTime).format('YYYY-MM-DD HH:mm')}
                  </span>
                </div>
                <div className="comment-content">{comment.commentContent}</div>
              </div>
            ))}
            {comments.length === 0 && (
              <div className="no-comments">暂无评论，快来抢沙发！</div>
            )}
          </div>
        </section>
      </div>
    </div>
  );
}
