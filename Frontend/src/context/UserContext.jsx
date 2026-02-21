import { createContext, useContext, useState, useEffect, useCallback } from "react";
import { login as apiLogin, register as apiRegister } from "../api/authApi";

const UserContext = createContext();

const ROLE_CONFIG = {
    ORGANIZER: { label: 'Organizer', color: '#6366f1' },
    ATTENDEE: { label: 'Attendee', color: '#10b981' },
    STAFF: { label: 'Staff', color: '#f59e0b' }
};

export function UserProvider({ children }) {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const storedUser = localStorage.getItem('user');
        const token = localStorage.getItem('token');
        if (storedUser && token) {
            try {
                setUser(JSON.parse(storedUser));
            } catch {
                localStorage.removeItem('user');
                localStorage.removeItem('token');
            }
        }
        setLoading(false);
    }, []);

    const login = useCallback(async (email, password) => {
        const response = await apiLogin(email, password);
        const userData = {
            id: response.userId,
            email: response.email,
            name: response.name,
            role: response.role,
            ...ROLE_CONFIG[response.role]
        };
        localStorage.setItem('token', response.token);
        localStorage.setItem('user', JSON.stringify(userData));
        setUser(userData);
        return userData;
    }, []);

    const register = useCallback(async (name, email, password, role) => {
        const response = await apiRegister(name, email, password, role);
        const userData = {
            id: response.userId,
            email: response.email,
            name: response.name,
            role: response.role,
            ...ROLE_CONFIG[response.role]
        };
        localStorage.setItem('token', response.token);
        localStorage.setItem('user', JSON.stringify(userData));
        setUser(userData);
        return userData;
    }, []);

    const logout = useCallback(() => {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        setUser(null);
    }, []);

    const isAuthenticated = !!user;
    const currentRole = user?.role || null;

    return (
        <UserContext.Provider value={{ user, currentRole, isAuthenticated, loading, login, register, logout }}>
            {children}
        </UserContext.Provider>
    );
}

export function useUser() {
    const context = useContext(UserContext);
    if (!context) {
        throw new Error('useUser must be used within a UserProvider');
    }
    return context;
}
