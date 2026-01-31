import {useState,useEffect} from 'react';
import {useNavigate} from 'react-router-dom';
import {getMyEvents,publishEvent} from "../api/eventApi";
import EventCard from "../components/EventCard";

function OrganizerDashboard(){
    const [events,setEvents]=useState([]);
    const [loading,setLoading]=useState(true);
    const [error,setError]=useState(null);
    const navigate=useNavigate();

    const fetchEvents=async()=>{
        try{
            setLoading(true);
            const data=await getMyEvents();
            setEvents(data);
            setError(null);
        }catch(err){
            setError(err.message||'Failed to fetch events');
        }finally{
            setLoading(false);
        }
    }
    useEffect(()=>{
        fetchEvents();
    },[]);
    const handlePublish=async(id)=>{
        try{
            await publishEvent(id);
            fetchEvents();
        }catch(err){
            alert(err.message||'Failed to publish event');
        }
    }
    const handleClose=async(id)=>{
        if(window.confirm('Are you sure?')){
            try {
                await deleteEvent(id);
                fetchEvents();
            }catch(err){
                alert(err.message||'Failed to close event');
            }
        }
    }
    const handleAddTicketType=(eventId) => {
        navigate(`/events/${eventId}/add-ticket-type`);
    };
    if(loading) return <div className="flex items-center justify-center p-16 text-slate-500 text-lg">Loading events...</div>;
    if(error) return <div className="max-w-xl mx-auto my-8 p-6 bg-red-100 text-red-800 rounded-lg text-center">Error: {error}</div>;
    return (
        <div className="p-8 max-w-6xl mx-auto">
            <div className="flex justify-between items-center mb-8">
                <h1 className="text-2xl font-bold text-white">My Events</h1>
                <button
                    className="px-6 py-3 bg-gradient-to-br from-indigo-500 to-violet-500 text-white rounded-lg font-semibold text-sm cursor-pointer transition-all duration-200 hover:-translate-y-px hover:shadow-lg hover:shadow-indigo-500/40"
                    onClick={() => navigate('/create-event')}
                >
                    + Create Event
                </button>
            </div>
            {events.length === 0 ? (
                <div className="text-center py-16 px-8 bg-slate-50 rounded-xl border-2 border-dashed border-slate-200">
                    <p className="text-slate-500 text-lg mb-6">You haven't created any events yet.</p>
                    <button
                        className="px-6 py-3 bg-gradient-to-br from-indigo-500 to-violet-500 text-white rounded-lg font-semibold cursor-pointer transition-all duration-200 hover:-translate-y-px hover:shadow-lg hover:shadow-indigo-500/40"
                        onClick={() => navigate('/create-event')}
                    >
                        Create Your First Event
                    </button>
                </div>
            ):(
                <div className="grid grid-cols-[repeat(auto-fill,minmax(350px,1fr))] gap-6">
                    {events.map((event) => (
                        <EventCard
                            key={event.id}
                            event={event}
                            showActions={true}
                            onPublish={handlePublish}
                            onClose={handleClose}
                            onAddTicketType={handleAddTicketType}
                        />
                    ))}
                </div>
            )}
        </div>
    );
}

export default OrganizerDashboard;

