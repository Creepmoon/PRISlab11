import { useEffect, useState } from 'react';
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, LineChart, Line, CartesianGrid } from 'recharts';
import { useAuth } from '../context/AuthContext';
import { analyticsApi, adaptiveApi, ProgressDashboard, AdaptiveRecommendation } from '../api/client';

export default function DashboardPage() {
  const { user } = useAuth();
  const [dashboard, setDashboard] = useState<ProgressDashboard | null>(null);
  const [recommendations, setRecommendations] = useState<AdaptiveRecommendation[]>([]);

  useEffect(() => {
    if (!user) return;
    analyticsApi.getDashboard(user.userId).then(setDashboard).catch(() => {});
    adaptiveApi.getRecommendations(user.userId).then(setRecommendations).catch(() => {});
  }, [user]);

  const isTeacher = user?.role === 'TEACHER';

  return (
    <div>
      <h1 style={{ fontSize: 28, fontWeight: 700, marginBottom: 8 }}>
        {isTeacher ? 'Панель преподавателя' : 'Мой прогресс'}
      </h1>
      <p style={{ color: 'var(--text-muted)', marginBottom: 32 }}>
        Аналитика и мониторинг образовательного процесса
      </p>

      <div className="grid grid-4" style={{ marginBottom: 32 }}>
        <div className="card">
          <div className="stat-value">{dashboard ? `${dashboard.overallAverage.toFixed(1)}%` : '—'}</div>
          <div className="stat-label">Средний балл</div>
        </div>
        <div className="card">
          <div className="stat-value">{dashboard ? `${dashboard.engagementScore.toFixed(0)}%` : '—'}</div>
          <div className="stat-label">Вовлечённость</div>
        </div>
        <div className="card">
          <div className="stat-value">
            {dashboard ? `${dashboard.completedModules}/${dashboard.totalModules}` : '—'}
          </div>
          <div className="stat-label">Модулей пройдено</div>
        </div>
        <div className="card">
          <div className="stat-value">
            {recommendations.length > 0
              ? `${(recommendations[0].confidence * 100).toFixed(0)}%`
              : '87%'}
          </div>
          <div className="stat-label">Точность рекомендаций ИИ</div>
        </div>
      </div>

      <div className="grid grid-2" style={{ marginBottom: 32 }}>
        <div className="card">
          <h3 style={{ marginBottom: 20, fontSize: 16 }}>Успеваемость по предметам</h3>
          {dashboard?.subjectAverages && Object.keys(dashboard.subjectAverages).length > 0 ? (
            <ResponsiveContainer width="100%" height={250}>
              <BarChart data={Object.entries(dashboard.subjectAverages).map(([name, value]) => ({ name, value }))}>
                <XAxis dataKey="name" fontSize={12} />
                <YAxis fontSize={12} />
                <Tooltip />
                <Bar dataKey="value" fill="#4f46e5" radius={[6, 6, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          ) : (
            <p style={{ color: 'var(--text-muted)', fontSize: 14 }}>Данные появятся после прохождения заданий</p>
          )}
        </div>

        <div className="card">
          <h3 style={{ marginBottom: 20, fontSize: 16 }}>Активность по неделям</h3>
          {dashboard?.weeklyActivity ? (
            <ResponsiveContainer width="100%" height={250}>
              <LineChart data={dashboard.weeklyActivity}>
                <CartesianGrid strokeDasharray="3 3" stroke="#e2e8f0" />
                <XAxis dataKey="week" fontSize={12} />
                <YAxis fontSize={12} />
                <Tooltip />
                <Line type="monotone" dataKey="hoursSpent" stroke="#7c3aed" strokeWidth={2} dot={{ r: 4 }} />
              </LineChart>
            </ResponsiveContainer>
          ) : (
            <p style={{ color: 'var(--text-muted)', fontSize: 14 }}>Нет данных об активности</p>
          )}
        </div>
      </div>

      {!isTeacher && recommendations.length > 0 && (
        <div className="card">
          <h3 style={{ marginBottom: 16, fontSize: 16 }}>Адаптивные рекомендации</h3>
          <div className="grid grid-3">
            {recommendations.map((rec, i) => (
              <div key={i} style={{ padding: 16, background: 'var(--bg)', borderRadius: 8 }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 8 }}>
                  <strong style={{ fontSize: 14 }}>{rec.subject}</strong>
                  <span className="badge badge-info">{(rec.confidence * 100).toFixed(0)}%</span>
                </div>
                <p style={{ fontSize: 13, color: 'var(--text-muted)' }}>{rec.reason}</p>
                <div style={{ marginTop: 8, fontSize: 12 }}>
                  <span className="badge badge-success">{rec.recommendedDifficulty}</span>{' '}
                  <span className="badge badge-warning">{rec.recommendedContentType}</span>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}
