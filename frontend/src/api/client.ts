const API_BASE = import.meta.env.VITE_API_URL || '/api';

export interface ApiResponse<T> {
  success: boolean;
  message?: string;
  data?: T;
  error?: string;
}

export interface AuthData {
  token: string;
  userId: string;
  email: string;
  fullName: string;
  role: 'STUDENT' | 'TEACHER' | 'ADMIN';
  expiresIn: number;
}

function getToken(): string | null {
  return localStorage.getItem('edusphere_token');
}

export async function api<T>(path: string, options: RequestInit = {}): Promise<T> {
  const token = getToken();
  const headers: Record<string, string> = {
    'Content-Type': 'application/json',
    ...(options.headers as Record<string, string>),
  };
  if (token) headers['Authorization'] = `Bearer ${token}`;

  const res = await fetch(`${API_BASE}${path}`, { ...options, headers });
  const json: ApiResponse<T> = await res.json();

  if (!json.success) {
    throw new Error(json.error || 'Request failed');
  }
  return json.data as T;
}

export const authApi = {
  login: (email: string, password: string) =>
    api<AuthData>('/auth/login', { method: 'POST', body: JSON.stringify({ email, password }) }),
  register: (email: string, password: string, fullName: string, role: string) =>
    api<AuthData>('/auth/register', {
      method: 'POST',
      body: JSON.stringify({ email, password, fullName, role }),
    }),
};

export const materialsApi = {
  getAll: () => api<Material[]>('/materials'),
};

export const curriculumApi = {
  generate: (data: GeneratePlanRequest) =>
    api<LearningPlan>('/curriculum/plans/generate', { method: 'POST', body: JSON.stringify(data) }),
  getStudentPlans: (studentId: string) => api<LearningPlan[]>(`/curriculum/plans/student/${studentId}`),
  approve: (planId: string, teacherId: string) =>
    api<LearningPlan>(`/curriculum/plans/${planId}/approve?teacherId=${teacherId}`, {
      method: 'PUT',
      body: JSON.stringify({}),
    }),
};

export const gradingApi = {
  getGrades: (studentId: string) => api<Grade[]>(`/grading/grades/student/${studentId}`),
  submitGrade: (grade: Partial<Grade>) =>
    api<Grade>('/grading/grades', { method: 'POST', body: JSON.stringify(grade) }),
};

export const analyticsApi = {
  getDashboard: (studentId: string) => api<ProgressDashboard>(`/analytics/dashboard/student/${studentId}`),
};

export const adaptiveApi = {
  getRecommendations: (studentId: string) =>
    api<AdaptiveRecommendation[]>(`/adaptive/recommendations/${studentId}`),
};

export const arApi = {
  getActive: () => api<ArSession[]>('/ar/sessions/active'),
  start: (id: string) => api<ArSession>(`/ar/sessions/${id}/start`, { method: 'POST' }),
};

export interface Material {
  id: string;
  title: string;
  description: string;
  subject: string;
  contentType: string;
  difficulty: string;
}

export interface LearningPlan {
  id: string;
  title: string;
  goals: string;
  status: string;
  aiConfidenceScore: number;
  modules: { name: string; subject: string; orderIndex: number; estimatedHours: number }[];
}

export interface GeneratePlanRequest {
  studentId: string;
  goals: string;
  interests: string[];
  subjects: string[];
  weeklyHours: number;
}

export interface Grade {
  id: string;
  subject: string;
  score: number;
  maxScore: number;
  feedback: string;
}

export interface ProgressDashboard {
  overallAverage: number;
  engagementScore: number;
  completedModules: number;
  totalModules: number;
  subjectAverages: Record<string, number>;
  skills: { skillName: string; level: number; growth: number }[];
  weeklyActivity: { week: string; hoursSpent: number; assignmentsCompleted: number }[];
}

export interface AdaptiveRecommendation {
  subject: string;
  recommendedDifficulty: string;
  recommendedContentType: string;
  confidence: number;
  reason: string;
}

export interface ArSession {
  id: string;
  title: string;
  subject: string;
  status: string;
  arModelUrl: string;
  arSupported: boolean;
}
