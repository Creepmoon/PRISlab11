import { useEffect, useState } from 'react';
import { arApi, ArSession } from '../api/client';

export default function ArClassPage() {
  const [sessions, setSessions] = useState<ArSession[]>([]);
  const [arSupported, setArSupported] = useState(false);

  useEffect(() => {
    arApi.getActive().then(setSessions).catch(() => setSessions([]));
    setArSupported(
      'xr' in navigator ||
      /Android|iPhone|iPad/i.test(navigator.userAgent)
    );
  }, []);

  const handleStart = async (id: string) => {
    try {
      await arApi.start(id);
      alert('AR-сессия запущена. Откройте мобильное приложение EduSphere AR для полного опыта.');
    } catch {
      alert('Не удалось запустить сессию');
    }
  };

  return (
    <div>
      <h1 style={{ fontSize: 28, fontWeight: 700, marginBottom: 8 }}>Виртуальный AR-класс</h1>
      <p style={{ color: 'var(--text-muted)', marginBottom: 32 }}>
        Занятия с дополненной реальностью (Android 14+, iOS 18+)
      </p>

      <div className="card" style={{ marginBottom: 24, display: 'flex', alignItems: 'center', gap: 16 }}>
        <span style={{ fontSize: 32 }}>🥽</span>
        <div>
          <strong>AR-поддержка устройства:</strong>{' '}
          <span className={`badge ${arSupported ? 'badge-success' : 'badge-warning'}`}>
            {arSupported ? 'Поддерживается' : 'Требуется мобильное устройство'}
          </span>
          <p style={{ fontSize: 13, color: 'var(--text-muted)', marginTop: 4 }}>
            Целевые показатели: &gt;30 FPS, задержка &lt;150 мс
          </p>
        </div>
      </div>

      {sessions.length === 0 ? (
        <div className="card" style={{ textAlign: 'center', padding: 48 }}>
          <p style={{ fontSize: 48, marginBottom: 16 }}>🎓</p>
          <p style={{ color: 'var(--text-muted)' }}>Нет активных AR-сессий</p>
        </div>
      ) : (
        <div className="grid grid-2">
          {sessions.map(s => (
            <div key={s.id} className="card">
              <h3 style={{ fontSize: 16, marginBottom: 8 }}>{s.title}</h3>
              <p style={{ fontSize: 13, color: 'var(--text-muted)', marginBottom: 12 }}>{s.subject}</p>
              <div style={{ display: 'flex', gap: 8, marginBottom: 16 }}>
                <span className="badge badge-success">{s.status}</span>
                {s.arSupported && <span className="badge badge-info">AR Ready</span>}
              </div>
              <button className="btn btn-primary" onClick={() => handleStart(s.id)}>
                Присоединиться к AR-классу
              </button>
            </div>
          ))}
        </div>
      )}

      <div className="card" style={{ marginTop: 32 }}>
        <h3 style={{ marginBottom: 12, fontSize: 16 }}>Web AR Preview</h3>
        <div style={{
          height: 300, background: '#1e1e2e', borderRadius: 8,
          display: 'flex', alignItems: 'center', justifyContent: 'center',
          color: '#a0a0b0', fontSize: 14,
        }}>
          <div style={{ textAlign: 'center' }}>
            <p style={{ fontSize: 48, marginBottom: 12 }}>📱</p>
            <p>Для полноценного AR-опыта используйте мобильное приложение</p>
            <p style={{ fontSize: 12, marginTop: 8 }}>mobile/ — React Native + ARKit/ARCore</p>
          </div>
        </div>
      </div>
    </div>
  );
}
