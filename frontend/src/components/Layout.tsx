import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import HelpPanel from './HelpPanel';

export default function Layout({ children }: { children: React.ReactNode }) {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const navItems = user?.role === 'TEACHER'
    ? [
        { to: '/dashboard', label: 'Панель' },
        { to: '/materials', label: 'Материалы' },
        { to: '/plans', label: 'Учебные планы' },
        { to: '/grading', label: 'Оценки' },
      ]
    : user?.role === 'ADMIN'
    ? [
        { to: '/dashboard', label: 'Аналитика' },
        { to: '/materials', label: 'Материалы' },
      ]
    : [
        { to: '/dashboard', label: 'Мой прогресс' },
        { to: '/materials', label: 'Материалы' },
        { to: '/plans', label: 'Мой план' },
        { to: '/ar', label: 'AR-класс' },
      ];

  return (
    <div>
      <header style={{
        background: 'var(--surface)',
        borderBottom: '1px solid var(--border)',
        padding: '12px 0',
        position: 'sticky',
        top: 0,
        zIndex: 100,
      }}>
        <div className="container" style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
          <Link to="/dashboard" style={{ display: 'flex', alignItems: 'center', gap: 10, color: 'var(--text)', fontWeight: 700, fontSize: 20 }}>
            <span style={{
              width: 36, height: 36, borderRadius: 10,
              background: 'linear-gradient(135deg, #4f46e5, #7c3aed)',
              display: 'flex', alignItems: 'center', justifyContent: 'center',
              color: '#fff', fontSize: 18,
            }}>E</span>
            EduSphere
          </Link>
          <nav style={{ display: 'flex', gap: 24, alignItems: 'center' }}>
            {navItems.map(item => (
              <Link key={item.to} to={item.to} style={{ color: 'var(--text-muted)', fontSize: 14, fontWeight: 500 }}>
                {item.label}
              </Link>
            ))}
            <HelpPanel />
            <span style={{ fontSize: 13, color: 'var(--text-muted)' }}>{user?.fullName}</span>
            <button className="btn btn-outline" onClick={handleLogout} style={{ padding: '6px 14px', fontSize: 13 }}>
              Выйти
            </button>
          </nav>
        </div>
      </header>
      <main style={{ padding: '32px 0', minHeight: 'calc(100vh - 60px)' }}>
        <div className="container">{children}</div>
      </main>
    </div>
  );
}
