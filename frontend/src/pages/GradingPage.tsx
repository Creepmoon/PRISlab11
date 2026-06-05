import { useState } from 'react';
import { gradingApi } from '../api/client';
import { useAuth } from '../context/AuthContext';

export default function GradingPage() {
  const { user } = useAuth();
  const [studentId, setStudentId] = useState('');
  const [subject, setSubject] = useState('');
  const [score, setScore] = useState('');
  const [maxScore, setMaxScore] = useState('100');
  const [feedback, setFeedback] = useState('');
  const [message, setMessage] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!user) return;
    try {
      await gradingApi.submitGrade({
        studentId,
        teacherId: user.userId,
        subject,
        score: parseInt(score),
        maxScore: parseInt(maxScore),
        feedback,
      });
      setMessage('Оценка успешно выставлена');
      setScore('');
      setFeedback('');
    } catch {
      setMessage('Ошибка при выставлении оценки');
    }
  };

  return (
    <div>
      <h1 style={{ fontSize: 28, fontWeight: 700, marginBottom: 8 }}>Выставление оценок</h1>
      <p style={{ color: 'var(--text-muted)', marginBottom: 32 }}>
        Контроль образовательного процесса — оценка заданий и работ
      </p>

      <div className="card" style={{ maxWidth: 500 }}>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label className="label">ID ученика (UUID)</label>
            <input className="input" value={studentId} onChange={e => setStudentId(e.target.value)} required />
          </div>
          <div className="form-group">
            <label className="label">Предмет</label>
            <input className="input" value={subject} onChange={e => setSubject(e.target.value)} required />
          </div>
          <div className="grid grid-2">
            <div className="form-group">
              <label className="label">Балл</label>
              <input className="input" type="number" value={score} onChange={e => setScore(e.target.value)} required />
            </div>
            <div className="form-group">
              <label className="label">Макс. балл</label>
              <input className="input" type="number" value={maxScore} onChange={e => setMaxScore(e.target.value)} required />
            </div>
          </div>
          <div className="form-group">
            <label className="label">Комментарий</label>
            <textarea className="input" rows={3} value={feedback} onChange={e => setFeedback(e.target.value)} />
          </div>
          <button className="btn btn-primary" type="submit">Выставить оценку</button>
          {message && <p style={{ marginTop: 12, fontSize: 13, color: 'var(--success)' }}>{message}</p>}
        </form>
      </div>
    </div>
  );
}
