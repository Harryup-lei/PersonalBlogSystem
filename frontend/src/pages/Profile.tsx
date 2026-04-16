import React, { useState, useEffect } from 'react';
import { useAuth } from '@/context/AuthContext';
import { userApi } from '@/utils/api';
import { Button } from '@/components/Button';
import { User as UserIcon, Mail, FileText, Save } from 'lucide-react';
import './Profile.css';

export function Profile() {
  const { user, updateUser } = useAuth();
  const [formData, setFormData] = useState({
    nickname: '',
    email: '',
    description: '',
  });
  const [avatar, setAvatar] = useState('');
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState({ type: '', text: '' });

  useEffect(() => {
    if (user) {
      setFormData({
        nickname: user.nickname || '',
        email: user.email || '',
        description: user.description || '',
      });
      setAvatar(user.avatar || '');
    }
  }, [user]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleAvatarChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = () => {
        setAvatar(reader.result as string);
      };
      reader.readAsDataURL(file);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setMessage({ type: '', text: '' });

    try {
      const res = await userApi.updateUserInfo({
        nickname: formData.nickname,
        avatar: avatar,
        description: formData.description,
      });
      updateUser(res.data);
      setMessage({ type: 'success', text: '保存成功！' });
    } catch (err: any) {
      setMessage({ type: 'error', text: err.message || '保存失败' });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="profile">
      <div className="container">
        <div className="profile-card">
          <div className="profile-header">
            <div className="avatar-section">
              <div className="avatar-wrapper">
                {avatar ? (
                  <img src={avatar} alt="头像" className="avatar-img" />
                ) : (
                  <div className="avatar-placeholder">
                    <UserIcon size={48} />
                  </div>
                )}
                <label className="avatar-upload">
                  <input
                    type="file"
                    accept="image/*"
                    onChange={handleAvatarChange}
                  />
                </label>
              </div>
            </div>
            <div className="user-info">
              <h2>{user?.nickname || user?.username}</h2>
              <p>@{user?.username}</p>
            </div>
          </div>

          <form onSubmit={handleSubmit} className="profile-form">
            {message.text && (
              <div className={`message ${message.type}`}>
                {message.text}
              </div>
            )}

            <div className="form-group">
              <label htmlFor="nickname">昵称</label>
              <input
                type="text"
                id="nickname"
                name="nickname"
                value={formData.nickname}
                onChange={handleChange}
                placeholder="请输入昵称"
              />
            </div>

            <div className="form-group">
              <label htmlFor="email">
                <Mail size={16} />
                邮箱
              </label>
              <input
                type="email"
                id="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                placeholder="请输入邮箱"
              />
            </div>

            <div className="form-group">
              <label htmlFor="description">
                <FileText size={16} />
                简介
              </label>
              <textarea
                id="description"
                name="description"
                value={formData.description}
                onChange={handleChange}
                placeholder="介绍一下自己吧..."
                rows={4}
              />
            </div>

            <Button type="submit" loading={loading} className="save-btn">
              <Save size={18} />
              保存修改
            </Button>
          </form>
        </div>
      </div>
    </div>
  );
}
