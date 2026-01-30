import {Link,useLocation} from "react-router-dom";
import {useUser} from "./context/UserContext";

function Navbar() {
    const {currentRole, user} = useUser();
    const location = useLocation();

    const getNavLinks = () => {
        switch (currentRole) {
            case 'ORGANIZER':
                return [
                    {path: '/', label: 'My Events'},
                    {path: '/events', label: 'Create Event'}
                ];
            case 'ATTENDEE':
                return [
                    {path: '/', label: 'Browse Events'},
                    {path: '/my-tickets', label: 'My Tickets'}
                ];
            case 'STAFF':
                return [
                    {path: '/', label: 'Validate Tickets'}
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
            <div
                className="px-4 py-2 rounded-full font-semibold text-sm text-white"
                style={{backgroundColor: user.color}}
            >
                {user.label}
            </div>
        </nav>
    );
}
export default Navbar;