import { Link, useLocation, useNavigate } from "react-router-dom";
import { useUser } from "../context/UserContext";

const ROLE_COLORS = {
    ORGANIZER: '#6366f1',
    ATTENDEE: '#10b981',
    STAFF: '#f59e0b'
};

function Navbar() {
    const { currentRole, user, logout } = useUser();
    const location = useLocation();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    const getNavLinks = () => {
        switch (currentRole) {
            case 'ORGANIZER':
                return [
                    { path: '/', label: 'My Events' },
                    { path: '/create-event', label: 'Create Event' }
                ];
            case 'ATTENDEE':
                return [
                    { path: '/', label: 'Browse Events' },
                    { path: '/my-tickets', label: 'My Tickets' }
                ];
            case 'STAFF':
                return [
                    { path: '/', label: 'Validate Tickets' }
                ];
            default:
                return [];
        }
    }

    return (
        <nav
            className="flex items-center justify-between px-8 py-4 bg-gradient-to-br from-slate-800 to-slate-600 text-white shadow-lg">
            <div className="flex items-center gap-2 text-2xl font-bold">
                <span className="text-3xl">🎫</span>
                <span className="bg-gradient-to-br from-blue-400 to-violet-400 bg-clip-text text-transparent">
                    EventSphere
                </span>
            </div>
            <div className="flex gap-6">
                {getNavLinks().map((link) => (
                    <Link
                        key={link.path}
                        to={link.path}
                        className={`text-slate-400 no-underline font-medium px-4 py-2 rounded-md transition-all duration-200 hover:text-white hover:bg-white/10 ${location.pathname === link.path ? 'text-white bg-white/15' : ''
                            }`}
                    >
                        {link.label}
                    </Link>
                ))}
            </div>
            <div className="flex items-center gap-4">
                <div className="flex items-center gap-3">
                    <div className="text-right">
                        <div className="text-sm font-medium text-white">{user?.name}</div>
                        <div className="text-xs text-slate-400">{user?.email}</div>
                    </div>
                    <div
                        className="px-3 py-1 rounded-full font-semibold text-xs text-white"
                        style={{ backgroundColor: ROLE_COLORS[currentRole] || '#6366f1' }}
                    >
                        {currentRole}
                    </div>
                </div>
                <button
                    onClick={handleLogout}
                    className="px-4 py-2 text-sm font-medium text-slate-300 border border-slate-500 rounded-lg hover:bg-white/10 hover:text-white transition-all duration-200"
                >
                    Logout
                </button>
            </div>
        </nav>
    );
}

export default Navbar;