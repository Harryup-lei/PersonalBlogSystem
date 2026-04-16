import React, { useState, useEffect, useRef } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import ReactMarkdown from 'react-markdown';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { vscDarkPlus } from 'react-syntax-highlighter/dist/esm/styles/prism';
import { articleApi, categoryApi, tagApi, fileApi } from '@/utils/api';
import { Category, Tag } from '@/types';
import { Button } from '@/components/Button';
import { Save, Eye, Upload, X } from 'lucide-react';
import './Write.css';

export function Write() {
  const [searchParams] = useSearchParams();
  const articleId = searchParams.get('id');
  const isEditMode = !!articleId;
  const navigate = useNavigate();

  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [summary, setSummary] = useState('');
  const [cover, setCover] = useState('');
  const [categoryId, setCategoryId] = useState<number | null>(null);
  const [selectedTags, setSelectedTags] = useState<number[]>([]);
  const [isTop, setIsTop] = useState(false);
  const [categories, setCategories] = useState<Category[]>([]);
  const [tags, setTags] = useState<Tag[]>([]);
  const [showPreview, setShowPreview] = useState(false);
  const [loading, setLoading] = useState(false);
  const [fetching, setFetching] = useState(false);

  const fileInputRef = useRef<HTMLInputElement>(null);
  const mdFileInputRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    fetchCategoriesAndTags();
    if (isEditMode) {
      fetchArticle();
    }
  }, [articleId]);

  const fetchCategoriesAndTags = async () => {
    try {
      const [catRes, tagRes] = await Promise.all([
        categoryApi.getAllCategories(),
        tagApi.getAllTags(),
      ]);
      setCategories(catRes.data || []);
      setTags(tagRes.data || []);
    } catch (err) {
      console.error('获取数据失败', err);
    }
  };

  const fetchArticle = async () => {
    setFetching(true);
    try {
      const res = await articleApi.getArticleDetail(Number(articleId));
      const article = res.data;
      setTitle(article.articleTitle);
      setSummary(article.articleSummary || '');
      setCover(article.articleCover || '');
      setCategoryId(article.categoryId);
      setIsTop(article.isTop === 1);
      if (article.mdFilePath) {
        // 拼接完整 URL 加载文件内容
        const baseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';
        const fullUrl = article.mdFilePath.startsWith('http')
          ? article.mdFilePath
          : `${baseUrl}/${article.mdFilePath}`;
        try {
          const response = await fetch(fullUrl);
          if (response.ok) {
            const text = await response.text();
            setContent(text);
          } else {
            setContent(`[无法加载文件: ${article.mdFilePath}]`);
          }
        } catch {
          setContent(`[无法加载文件: ${article.mdFilePath}]`);
        }
      }
    } catch (err) {
      console.error('获取文章失败', err);
    } finally {
      setFetching(false);
    }
  };

  const handleCoverUpload = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      // 用 FileReader 本地预览，让用户立即看到图片
      const reader = new FileReader();
      reader.onload = (event) => {
        const localUrl = event.target?.result as string;
        setCover(localUrl);
      };
      reader.readAsDataURL(file);

      // 同时上传到服务器
      try {
        const res = await fileApi.uploadImage(file);
        if (res.code === 200 && res.data) {
          // 上传成功后，使用服务器返回的 URL 替换本地预览
          setCover(res.data.url);
        }
      } catch (err) {
        console.error('封面上传失败', err);
      }
    }
  };

  const handleMdFileSelect = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      // 读取文件内容到编辑器
      const reader = new FileReader();
      reader.onload = () => {
        setContent(reader.result as string);
      };
      reader.readAsText(file);
    }
  };

  const handleTagToggle = (tagId: number) => {
    setSelectedTags((prev) =>
      prev.includes(tagId)
        ? prev.filter((id) => id !== tagId)
        : [...prev, tagId]
    );
  };

  const handleSubmit = async (publishStatus: number) => {
    if (!title.trim()) {
      alert('请输入文章标题');
      return;
    }
    if (!categoryId) {
      alert('请选择文章分类');
      return;
    }
    if (!content.trim()) {
      alert('请输入文章内容');
      return;
    }

    setLoading(true);
    try {
      // 1. 先保存文件内容到服务器，获得文件路径
      const filename = title.replace(/[^a-zA-Z0-9\u4e00-\u9fa5]/g, '_');
      const saveRes = await fileApi.saveMarkdown({ content, filename });
      const mdFilePath = saveRes.code === 200 ? saveRes.data.path : '';

      // 2. 提交文章数据
      const data = {
        articleTitle: title,
        articleCover: cover,
        categoryId: categoryId!,
        articleSummary: summary,
        mdFilePath,
        isTop: isTop ? 1 : 0,
        status: publishStatus,
        tagIds: selectedTags,
      };

      if (isEditMode) {
        await articleApi.updateArticle({ id: Number(articleId), ...data });
      } else {
        await articleApi.publishArticle(data);
      }

      navigate('/');
    } catch (err: any) {
      alert(err.message || '保存失败');
    } finally {
      setLoading(false);
    }
  };

  if (fetching) {
    return <div className="write-loading">加载中...</div>;
  }

  return (
    <div className="write">
      <div className="write-container">
        <div className="write-header">
          <h1>{isEditMode ? '编辑文章' : '写文章'}</h1>
          <div className="write-actions">
            <Button
              variant="secondary"
              onClick={() => setShowPreview(!showPreview)}
            >
              <Eye size={18} />
              {showPreview ? '编辑' : '预览'}
            </Button>
            <Button
              variant="secondary"
              onClick={() => handleSubmit(0)}
              loading={loading}
            >
              <Save size={18} />
              保存草稿
            </Button>
            <Button onClick={() => handleSubmit(1)} loading={loading}>
              <Save size={18} />
              发布
            </Button>
          </div>
        </div>

        <div className="write-content">
          {!showPreview ? (
            <div className="editor-pane">
              <div className="form-group">
                <input
                  type="text"
                  placeholder="文章标题"
                  value={title}
                  onChange={(e) => setTitle(e.target.value)}
                  className="title-input"
                />
              </div>

              <div className="form-row">
                <div className="form-group">
                  <label>分类</label>
                  <select
                    value={categoryId || ''}
                    onChange={(e) => setCategoryId(Number(e.target.value))}
                  >
                    <option value="">选择分类</option>
                    {categories.map((cat) => (
                      <option key={cat.id} value={cat.id}>
                        {cat.categoryName}
                      </option>
                    ))}
                  </select>
                </div>

                <div className="form-group">
                  <label>标签</label>
                  <div className="tag-selector">
                    {tags.map((tag) => (
                      <button
                        key={tag.id}
                        type="button"
                        className={`tag-btn ${selectedTags.includes(tag.id) ? 'active' : ''}`}
                        onClick={() => handleTagToggle(tag.id)}
                      >
                        {tag.tagName}
                      </button>
                    ))}
                  </div>
                </div>
              </div>

              <div className="form-row">
                <div className="form-group">
                  <label>封面图片</label>
                  <div className="cover-uploader">
                    {cover ? (
                      <div className="cover-preview">
                        <img src={cover} alt="封面" />
                        <button
                          type="button"
                          className="remove-cover"
                          onClick={() => setCover('')}
                        >
                          <X size={16} />
                        </button>
                      </div>
                    ) : (
                      <button
                        type="button"
                        className="upload-btn"
                        onClick={() => fileInputRef.current?.click()}
                      >
                        <Upload size={20} />
                        上传封面
                      </button>
                    )}
                    <input
                      ref={fileInputRef}
                      type="file"
                      accept="image/*"
                      onChange={handleCoverUpload}
                      style={{ display: 'none' }}
                    />
                  </div>
                </div>

                <div className="form-group">
                  <label>Markdown文件</label>
                  <button
                    type="button"
                    className="upload-btn"
                    onClick={() => mdFileInputRef.current?.click()}
                  >
                    <Upload size={20} />
                    选择.md文件
                  </button>
                  <input
                    ref={mdFileInputRef}
                    type="file"
                    accept=".md"
                    onChange={handleMdFileSelect}
                    style={{ display: 'none' }}
                  />
                </div>
              </div>

              <div className="form-group">
                <label>摘要</label>
                <textarea
                  placeholder="文章摘要（选填）"
                  value={summary}
                  onChange={(e) => setSummary(e.target.value)}
                  className="summary-input"
                  rows={2}
                />
              </div>

              <div className="form-group">
                <label>
                  内容
                  <div className="markdown-toggle">
                    <input
                      type="checkbox"
                      id="isTop"
                      checked={isTop}
                      onChange={(e) => setIsTop(e.target.checked)}
                    />
                    <label htmlFor="isTop">置顶</label>
                  </div>
                </label>
                
                <textarea
                  placeholder="使用 Markdown 编写文章内容..."
                  value={content}
                  onChange={(e) => setContent(e.target.value)}
                  className="content-input"
                />
              </div>
            </div>
          ) : (
            <div className="preview-pane">
              <h2 className="preview-title">{title || '无标题'}</h2>
              <div className="preview-meta">
                <span>分类: {categories.find((c) => c.id === categoryId)?.categoryName || '未选择'}</span>
                {selectedTags.length > 0 && (
                  <span>标签: {tags.filter((t) => selectedTags.includes(t.id)).map((t) => t.tagName).join(', ')}</span>
                )}
              </div>
              {cover && <img src={cover} alt="封面" className="preview-cover" />}
              <div className="preview-content">
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
                  {content || '*暂无内容*'}
                </ReactMarkdown>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
