import {useState,useEffect} from 'react';
import {useNavigate} from 'react-router-dom';
import {getPublishedEvents} from '../api/eventApi'
import EventCard from '../components/EventCard'

function AttendeeDashboard(){
    const [events,setEvents]=useState([]);
    const [loading,setLoading]=useState(true);
    const [error,setError]=useState(null);
    const navigate = useNavigate();

    useEffect(()=>{
        const fetchEvents=async() =>{
            try{
                setLoading(true);
                const data=await getPublishedEvents();
                setEvents(data);
                setError(null);
            }catch(err){
                setError(err.message||'Failed to fetch events');
            }finally{
                setLoading(false);
            }
        };
        fetchEvents();
    },[]);

    if(loading) return <div className="flex items-center justify-center p-16 text-slate-500 text-lg">Loading events...</div>;
    if(error) return <div className="max-w-xl mx-auto my-8 p-6 bg-red-100 text-red-800 rounded-lg text-center">Error: {error}</div>;

    return(
        <div className="p-8 max-w-6xl mx-auto">
            <div className="flex justify-between items-center mb-8">
                <h1 className="text-2xl font-bold text-white">Browse Events</h1>
                <button
                    className="px-6 py-3 bg-slate-200 text-slate-600 rounded-lg font-semibold text-sm cursor-pointer transition-all duration-200 hover:bg-slate-300"
                    onClick={() => navigate('/my-tickets')}
                >
                    View My Tickets
                </button>
            </div>
            {events.length===0?(
                <div className="text-center py-16 px-8 bg-slate-50 rounded-xl border-2 border-dashed border-slate-200">
                    <p className="text-slate-500 text-lg">No events available right now. Check back later!</p>
                </div>
            ):(
                <div className="grid grid-cols-[repeat(auto-fill,minmax(350px,1fr))] gap-6">
                    {events.map((event) => (
                        <EventCard
                            key={event.id}
                            event={event}
                            showActions={false}
                        />
                    ))}
                </div>
            )}
        </div>
    );
}
export default AttendeeDashboard;