import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '@/context/AuthContext';
import { PenSquare, User, LogOut, Menu, X } from 'lucide-react';
import './Layout.css';

interface LayoutProps {
  children: React.ReactNode;
}

export function Layout({ children }: LayoutProps) {
  const { user, isAuthenticated, logout } = useAuth();
  const navigate = useNavigate();
  const [menuOpen, setMenuOpen] = React.useState(false);

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <div className="layout">
      <header className="header">
        <div className="header-container">
          <Link to="/" className="logo">
            我的博客
          </Link>

          <nav className={`nav ${menuOpen ? 'nav-open' : ''}`}>
            <Link to="/" className="nav-link" onClick={() => setMenuOpen(false)}>
              首页
            </Link>
            {isAuthenticated && (
              <Link to="/write" className="nav-link" onClick={() => setMenuOpen(false)}>
                <PenSquare size={18} />
                写文章
              </Link>
            )}
            {isAuthenticated ? (
              <>
                <Link to="/profile" className="nav-link" onClick={() => setMenuOpen(false)}>
                  <User size={18} />
                  {user?.nickname || user?.username}
                </Link>
                <button className="nav-link logout-btn" onClick={handleLogout}>
                  <LogOut size={18} />
                  退出
                </button>
              </>
            ) : (
              <>
                <Link to="/login" className="nav-link login-link" onClick={() => setMenuOpen(false)}>
                  登录
                </Link>
                <Link to="/register" className="nav-link register-link" onClick={() => setMenuOpen(false)}>
                  注册
                </Link>
              </>
            )}
          </nav>

          <button className="menu-toggle" onClick={() => setMenuOpen(!menuOpen)}>
            {menuOpen ? <X size={24} /> : <Menu size={24} />}
          </button>
        </div>
      </header>

      <main className="main">{children}</main>

      <footer className="footer">
        <div className="footer-container">
          <p>&copy; 2024 个人博客系统. All rights reserved.</p>
        </div>
      </footer>
    </div>
  );
}
