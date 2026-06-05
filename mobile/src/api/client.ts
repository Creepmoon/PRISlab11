const API_BASE = 'http://10.0.2.2:8080/api'; // Android emulator → localhost

export async function api<T>(path: string, options: RequestInit = {}): Promise<T> {
  const res = await fetch(`${API_BASE}${path}`, {
    ...options,
    headers: { 'Content-Type': 'application/json', ...(options.headers as object) },
  });
  const json = await res.json();
  if (!json.success) throw new Error(json.error || 'Request failed');
  return json.data;
}

export interface ArSession {
  id: string;
  title: string;
  subject: string;
  arModelUrl: string;
  status: string;
}

export const arApi = {
  getActive: () => api<ArSession[]>('/ar/sessions/active'),
  start: (id: string) => api<ArSession>(`/ar/sessions/${id}/start`, { method: 'POST' }),
  reportMetrics: (id: string, fps: number, latencyMs: number) =>
    api<ArSession>(`/ar/sessions/${id}/metrics?fps=${fps}&latencyMs=${latencyMs}`, { method: 'POST' }),
};
