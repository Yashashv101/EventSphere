import {createContext,useContext,useState,useEffect} from "react";
import {USERS,setCurrentUser} from "../api/axiosClient";

const UserContext=createContext();
const USER_ROLES={
    ORGANIZER:{
        id:USERS.ORGANIZER,
        email:'organizer@example.com',
        label:'Organizer',
        color:'#6366f1'
    },
    ATTENDEE:{
        id:USERS.ATTENDEE,
        email:'attendee@example.com',
        label:'Attendee',
        color:'#10b981'
    },
    STAFF:{
        id:USERS.STAFF,
        email:'staff@example.com',
        label:'Staff',
        color:'#f59e0b'
    }
}
export function UserProvider({ children }) {
    const [currentRole, setCurrentRole] = useState('ORGANIZER');
    const user = USER_ROLES[currentRole];
    useEffect(() => {
        setCurrentUser(user.id, user.email);
    }, [currentRole, user.id, user.email]);
    const switchRole = (role) => {
        if (USER_ROLES[role]) {
            setCurrentRole(role);
        }
    };
    const value = {
        currentRole,
        user,
        switchRole,
        roles: USER_ROLES
    };
    return (
        <UserContext.Provider value={value}>
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
export { USER_ROLES };
