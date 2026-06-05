import { createContext, useContext, useState, ReactNode } from 'react';
import { AuthData } from '../api/client';

interface AuthContextType {
  user: AuthData | null;
  login: (data: AuthData) => void;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<AuthData | null>(() => {
    const stored = localStorage.getItem('edusphere_user');
    return stored ? JSON.parse(stored) : null;
  });

  const login = (data: AuthData) => {
    localStorage.setItem('edusphere_token', data.token);
    localStorage.setItem('edusphere_user', JSON.stringify(data));
    setUser(data);
  };

  const logout = () => {
    localStorage.removeItem('edusphere_token');
    localStorage.removeItem('edusphere_user');
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
}
