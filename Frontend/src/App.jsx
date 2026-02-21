import OrganizerDashboard from "./pages/OrganizerDashboard.jsx";
import AttendeeDashboard from "./pages/AttendeeDashboard.jsx";
import StaffDashboard from "./pages/StaffDashboard.jsx";
import Navbar from "./components/Navbar.jsx";
import ProtectedRoute from "./components/ProtectedRoute.jsx";
import CreateEvent from "./pages/CreateEvent.jsx";
import EventDetails from "./pages/EventDetails.jsx";
import LoginPage from "./pages/LoginPage.jsx";
import RegisterPage from "./pages/RegisterPage.jsx";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import { UserProvider, useUser } from "./context/UserContext";
import MyTickets from './pages/MyTickets';

function AppContent() {
    const { isAuthenticated, currentRole, loading } = useUser();

    if (loading) {
        return (
            <div className="min-h-screen flex items-center justify-center bg-slate-900">
                <div className="text-slate-400 text-lg">Loading...</div>
            </div>
        );
    }

    const renderDashboard = () => {
        switch (currentRole) {
            case 'ORGANIZER':
                return <OrganizerDashboard />
            case 'ATTENDEE':
                return <AttendeeDashboard />
            case 'STAFF':
                return <StaffDashboard />
            default:
                return <AttendeeDashboard />
        }
    }

    return (
        <div className="min-h-screen flex flex-col bg-slate-900 text-slate-100">
            {isAuthenticated && <Navbar />}
            <main className={isAuthenticated ? "flex-1 px-8 py-6" : "flex-1"}>
                <Routes>
                    {/* Public routes */}
                    <Route path='/login' element={
                        isAuthenticated ? <Navigate to="/" replace /> : <LoginPage />
                    } />
                    <Route path='/register' element={
                        isAuthenticated ? <Navigate to="/" replace /> : <RegisterPage />
                    } />

                    {/* Protected routes */}
                    <Route path='/' element={
                        <ProtectedRoute>
                            {renderDashboard()}
                        </ProtectedRoute>
                    } />
                    <Route path='/create-event' element={
                        <ProtectedRoute allowedRoles={['ORGANIZER']}>
                            <CreateEvent />
                        </ProtectedRoute>
                    } />
                    <Route path='/events/:id' element={
                        <ProtectedRoute>
                            <EventDetails />
                        </ProtectedRoute>
                    } />
                    <Route path='/my-tickets' element={
                        <ProtectedRoute allowedRoles={['ATTENDEE']}>
                            <MyTickets />
                        </ProtectedRoute>
                    } />
                </Routes>
            </main>
        </div>
    )
}

function App() {
    return (
        <Router>
            <UserProvider>
                <AppContent />
            </UserProvider>
        </Router>
    );
}

export default App;
