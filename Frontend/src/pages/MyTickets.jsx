import {useState,useEffect} from 'react';
import {getMyTickets} from '../api/ticketApi';
import TicketCard from '../components/TicketCard';

function MyTickets(){
    const [tickets,setTickets]=useState([]);
    const [loading,setLoading]=useState(true);
    const [error,setError]=useState(null);

    useEffect(()=>{
        const fetchTickets=async ()=>{
            try{
                setLoading(true);
                const data=await getMyTickets();
                setTickets(data);
                setError(null);
            }catch(err){
                setError(err.message||'Failed to fetch tickets');
            }finally{
                setLoading(false);
            }
        };
        fetchTickets();
    },[]);
    if(loading) return <div className="flex items-center justify-center p-16 text-slate-500 text-lg">Loading your tickets...</div>;
    if(error) return <div className="max-w-xl mx-auto my-8 p-6 bg-red-100 text-red-800 rounded-lg text-center">Error: {error}</div>;
    return(
        <div className="p-8 max-w-6xl mx-auto">
            <div className="flex justify-between items-center mb-8">
                <h1 className="text-2xl font-bold text-slate-800">My Tickets</h1>
            </div>
            {tickets.length===0?(
                <div className="text-center py-16 px-8 bg-slate-50 rounded-xl border-2 border-dashed border-slate-200">
                    <p className="text-slate-500 text-lg">You haven't purchased any tickets yet.</p>
                    <p className="text-slate-500 text-lg mt-2">Browse events to find something exciting!</p>
                </div>
            ):(
                <div className="flex flex-col gap-6">
                    {tickets.map((ticket)=>(
                        <TicketCard key={ticket.id} ticket={ticket} />
                    ))}
                </div>
            )}
        </div>
    );
}

export default MyTickets;
