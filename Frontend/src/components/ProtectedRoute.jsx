import { Navigate } from 'react-router-dom';
import { useUser } from '../context/UserContext';

function ProtectedRoute({ children, allowedRoles }) {
    const { isAuthenticated, loading, currentRole } = useUser();

    if (loading) {
        return (
            <div className="flex items-center justify-center min-h-[50vh]">
                <div className="text-slate-400 text-lg">Loading...</div>
            </div>
        );
    }

    if (!isAuthenticated) {
        return <Navigate to="/login" replace />;
    }

    if (allowedRoles && !allowedRoles.includes(currentRole)) {
        return (
            <div className="flex flex-col items-center justify-center min-h-[50vh] gap-4">
                <div className="text-5xl">🚫</div>
                <h2 className="text-2xl font-bold text-white">Access Denied</h2>
                <p className="text-slate-400">You don't have permission to access this page.</p>
            </div>
        );
    }

    return children;
}

export default ProtectedRoute;
