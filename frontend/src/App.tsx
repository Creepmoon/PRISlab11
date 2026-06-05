import { Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import Layout from './components/Layout';
import LoginPage from './pages/LoginPage';
import DashboardPage from './pages/DashboardPage';
import MaterialsPage from './pages/MaterialsPage';
import PlansPage from './pages/PlansPage';
import GradingPage from './pages/GradingPage';
import ArClassPage from './pages/ArClassPage';

function PrivateRoute({ children }: { children: React.ReactNode }) {
  const { user } = useAuth();
  return user ? <Layout>{children}</Layout> : <Navigate to="/login" />;
}

export default function App() {
  return (
    <AuthProvider>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/dashboard" element={<PrivateRoute><DashboardPage /></PrivateRoute>} />
        <Route path="/materials" element={<PrivateRoute><MaterialsPage /></PrivateRoute>} />
        <Route path="/plans" element={<PrivateRoute><PlansPage /></PrivateRoute>} />
        <Route path="/grading" element={<PrivateRoute><GradingPage /></PrivateRoute>} />
        <Route path="/ar" element={<PrivateRoute><ArClassPage /></PrivateRoute>} />
        <Route path="*" element={<Navigate to="/dashboard" />} />
      </Routes>
    </AuthProvider>
  );
}
