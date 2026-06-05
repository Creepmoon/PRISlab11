import { useState } from 'react';

const HELP_SECTIONS = [
  {
    title: 'Создание учебного плана',
    content: 'Перейдите в раздел «Мой план», укажите цели обучения и интересы. Система сгенерирует индивидуальную программу с помощью ИИ. Преподаватель может внести правки.',
  },
  {
    title: 'AR-класс',
    content: 'Откройте раздел «AR-класс» на мобильном устройстве с поддержкой AR (Android 14+, iOS 18+). Наведите камеру на поверхность для размещения 3D-модели.',
  },
  {
    title: 'Анализ дашбордов',
    content: 'На панели прогресса отображаются оценки по предметам, вовлечённость и рекомендации адаптивного обучения.',
  },
];

export default function HelpPanel() {
  const [open, setOpen] = useState(false);

  return (
    <div style={{ position: 'relative' }}>
      <button
        className="btn btn-outline"
        onClick={() => setOpen(!open)}
        style={{ padding: '6px 14px', fontSize: 13 }}
        title="Интерактивная справка"
      >
        ?
      </button>
      {open && (
        <div className="card" style={{
          position: 'absolute', right: 0, top: '100%', marginTop: 8,
          width: 360, zIndex: 200, maxHeight: 400, overflow: 'auto',
        }}>
          <h3 style={{ marginBottom: 16, fontSize: 16 }}>Справка EduSphere</h3>
          {HELP_SECTIONS.map((s, i) => (
            <div key={i} style={{ marginBottom: 16 }}>
              <strong style={{ fontSize: 14 }}>{s.title}</strong>
              <p style={{ fontSize: 13, color: 'var(--text-muted)', marginTop: 4 }}>{s.content}</p>
            </div>
          ))}
          <a href="/docs/user-manual.pdf" style={{ fontSize: 13 }}>Скачать PDF-руководство</a>
        </div>
      )}
    </div>
  );
}
