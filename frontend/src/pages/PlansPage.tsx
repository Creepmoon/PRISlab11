import { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { curriculumApi, LearningPlan } from '../api/client';

export default function PlansPage() {
  const { user } = useAuth();
  const [plans, setPlans] = useState<LearningPlan[]>([]);
  const [goals, setGoals] = useState('');
  const [interests, setInterests] = useState('');
  const [generating, setGenerating] = useState(false);

  const loadPlans = () => {
    if (!user) return;
    curriculumApi.getStudentPlans(user.userId).then(setPlans).catch(() => setPlans([]));
  };

  useEffect(loadPlans, [user]);

  const handleGenerate = async () => {
    if (!user) return;
    setGenerating(true);
    try {
      const plan = await curriculumApi.generate({
        studentId: user.userId,
        goals,
        interests: interests.split(',').map(s => s.trim()).filter(Boolean),
        subjects: ['Mathematics', 'Programming', 'Physics'],
        weeklyHours: 10,
      });
      setPlans(prev => [plan, ...prev]);
    } catch {
      alert('Ошибка генерации плана');
    } finally {
      setGenerating(false);
    }
  };

  const handleApprove = async (planId: string) => {
    if (!user) return;
    try {
      await curriculumApi.approve(planId, user.userId);
      loadPlans();
    } catch {
      alert('Ошибка утверждения');
    }
  };

  const isTeacher = user?.role === 'TEACHER';

  return (
    <div>
      <h1 style={{ fontSize: 28, fontWeight: 700, marginBottom: 8 }}>
        {isTeacher ? 'Конструктор учебных планов' : 'Мой учебный план'}
      </h1>
      <p style={{ color: 'var(--text-muted)', marginBottom: 32 }}>
        Индивидуальная образовательная программа с ИИ-генерацией
      </p>

      {!isTeacher && (
        <div className="card" style={{ marginBottom: 32 }}>
          <h3 style={{ marginBottom: 16, fontSize: 16 }}>Сгенерировать новый план</h3>
          <div className="form-group">
            <label className="label">Цели обучения</label>
            <textarea className="input" rows={3} value={goals} onChange={e => setGoals(e.target.value)}
              placeholder="Например: освоить основы программирования на Python" />
          </div>
          <div className="form-group">
            <label className="label">Интересы (через запятую)</label>
            <input className="input" value={interests} onChange={e => setInterests(e.target.value)}
              placeholder="AI, робототехника, математика" />
          </div>
          <button className="btn btn-primary" onClick={handleGenerate} disabled={generating || !goals}>
            {generating ? 'Генерация ИИ...' : 'Сгенерировать план'}
          </button>
        </div>
      )}

      {plans.length === 0 ? (
        <div className="card" style={{ textAlign: 'center', padding: 48 }}>
          <p style={{ color: 'var(--text-muted)' }}>Учебные планы отсутствуют</p>
        </div>
      ) : (
        plans.map(plan => (
          <div key={plan.id} className="card" style={{ marginBottom: 20 }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'start', marginBottom: 16 }}>
              <div>
                <h3 style={{ fontSize: 18 }}>{plan.title}</h3>
                <p style={{ fontSize: 13, color: 'var(--text-muted)', marginTop: 4 }}>{plan.goals}</p>
              </div>
              <div style={{ display: 'flex', gap: 8, alignItems: 'center' }}>
                <span className={`badge ${plan.status === 'TEACHER_APPROVED' ? 'badge-success' : 'badge-warning'}`}>
                  {plan.status}
                </span>
                <span className="badge badge-info">ИИ: {(plan.aiConfidenceScore * 100).toFixed(0)}%</span>
                {isTeacher && plan.status !== 'TEACHER_APPROVED' && (
                  <button className="btn btn-primary" style={{ padding: '6px 14px', fontSize: 13 }}
                    onClick={() => handleApprove(plan.id)}>
                    Утвердить
                  </button>
                )}
              </div>
            </div>
            <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
              {plan.modules?.map((mod, i) => (
                <div key={i} style={{
                  display: 'flex', alignItems: 'center', gap: 12,
                  padding: '12px 16px', background: 'var(--bg)', borderRadius: 8,
                }}>
                  <span style={{
                    width: 28, height: 28, borderRadius: '50%', background: 'var(--primary)',
                    color: '#fff', display: 'flex', alignItems: 'center', justifyContent: 'center',
                    fontSize: 12, fontWeight: 600, flexShrink: 0,
                  }}>{mod.orderIndex}</span>
                  <div style={{ flex: 1 }}>
                    <strong style={{ fontSize: 14 }}>{mod.name}</strong>
                    <span style={{ fontSize: 12, color: 'var(--text-muted)', marginLeft: 8 }}>{mod.subject}</span>
                  </div>
                  <span style={{ fontSize: 12, color: 'var(--text-muted)' }}>{mod.estimatedHours}ч</span>
                </div>
              ))}
            </div>
          </div>
        ))
      )}
    </div>
  );
}
