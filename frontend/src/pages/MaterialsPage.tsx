import { useEffect, useState } from 'react';
import { materialsApi, Material } from '../api/client';

const CONTENT_ICONS: Record<string, string> = {
  TEXT: '📄', NUMERIC: '🔢', IMAGE: '🖼️', AUDIO: '🎵', VIDEO: '🎬', AR_MODEL: '🥽',
};

export default function MaterialsPage() {
  const [materials, setMaterials] = useState<Material[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    materialsApi.getAll()
      .then(setMaterials)
      .catch(() => setMaterials([]))
      .finally(() => setLoading(false));
  }, []);

  return (
    <div>
      <h1 style={{ fontSize: 28, fontWeight: 700, marginBottom: 8 }}>Образовательные материалы</h1>
      <p style={{ color: 'var(--text-muted)', marginBottom: 32 }}>
        Текстовые, числовые, графические, звуковые и видеоматериалы
      </p>

      {loading ? (
        <p>Загрузка...</p>
      ) : materials.length === 0 ? (
        <div className="card" style={{ textAlign: 'center', padding: 48 }}>
          <p style={{ fontSize: 48, marginBottom: 16 }}>📚</p>
          <p style={{ color: 'var(--text-muted)' }}>Материалы пока не добавлены</p>
        </div>
      ) : (
        <div className="grid grid-3">
          {materials.map(m => (
            <div key={m.id} className="card">
              <div style={{ fontSize: 32, marginBottom: 12 }}>
                {CONTENT_ICONS[m.contentType] || '📄'}
              </div>
              <h3 style={{ fontSize: 16, marginBottom: 8 }}>{m.title}</h3>
              <p style={{ fontSize: 13, color: 'var(--text-muted)', marginBottom: 12 }}>{m.description}</p>
              <div style={{ display: 'flex', gap: 8 }}>
                <span className="badge badge-info">{m.subject}</span>
                <span className="badge badge-success">{m.difficulty}</span>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
