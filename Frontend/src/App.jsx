import './App.css'
import OrganizerDashboard from "./pages/OrganizerDashboard.jsx";
import AttendeeDashboard from "./pages/AttendeeDashboard.jsx";
import StaffDashboard from "./pages/StaffDashboard.jsx";
import Navbar from "./components/Navbar.jsx";
import CreateEvent from "./pages/CreateEvent.jsx";
import EventDetails from "./pages/EventDetails.jsx";
import {Routes} from "react-router-dom";
import {useUser} from "./context/UserContext";

function AppContent() {
  const {currentRole}=useUser();
  const renderDashboard=()=>{
      switch(currentRole) {
          case 'ORGANIZER':
              return <OrganizerDashboard/>
          case 'ATTENDEE':
              return <AttendeeDashboard/>
          case 'STAFF':
              return <StaffDashboard/>
          default:
              return <AttendeeDashboard/>
      }
  }
  return(
      <div className="min-h-screen flex flex-col bg-slate-100 text-slate-800 font-sans">
          <Navbar/>
          <main className="flex-1 px-8 py-4 pb-8">
              <Routes>
                  <Route path='/' element={renderDasboard()}/>
                  <Route path='/create-event' element={<CreateEvent/>}/>
                  <Route path='/events/:id' element={<EventDetails/>}/>
                  <Route path='/my-tickets' element={<MyTickets/>}/>
              </Routes>
          </main>
      </div>
  )
}
function App(){
    return(
        <Router>
            <UserProvider>
                <AppContent />
            </UserProvider>
        </Router>
    );
}
export default App;
