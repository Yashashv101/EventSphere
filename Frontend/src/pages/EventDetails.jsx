import {useState,useEffect} from 'react';
import {useParams,useNavigate} from 'react-router-dom';
import {getEventDetails,publishEvent,addTicketType} from '../api/eventApi';
import {purchaseTicket} from '../api/ticketApi';
import {useUser} from '../context/UserContext';

function EventDetails(){
    const {id}=useParams();
    const navigate=useNavigate();
    const {currentRole}=useUser();
    const [event,setEvent]=useState(null);
    const [loading,setLoading]=useState(true);
    const [error, setError]=useState(null);
    const [showTicketForm,setShowTicketForm]=useState(false);
    const [ticketFormData,setTicketFormData]=useState({
        name:'',
        description:'',
        price:'',
        totalQuantity:''
    });
    const [purchasing,setPurchasing]=useState(null);
    const fetchEvent=async()=>{
        try{
            setLoading(true);
            const data=await getEventDetails(id);
            setEvent(data);
            setError(null);
        } catch(err){
            setError(err.message || 'Failed to fetch event');
        }finally{
            setLoading(false);
        }
    };
    useEffect(()=>{
        fetchEvent();
    },[id]);

    const handlePublish=async()=>{
        try{
            await publishEvent(id);
            fetchEvent();
        }catch(err){
            alert(err.message||'Failed to publish event');
        }
    };

    const handleAddTicketType=async (e)=>{
        e.preventDefault();
        try{
            await addTicketType(id,{
                name:ticketFormData.name,
                description:ticketFormData.description,
                price:parseFloat(ticketFormData.price),
                totalQuantity:parseInt(ticketFormData.totalQuantity)
            });
            setShowTicketForm(false);
            setTicketFormData({name:'',description:'',price:'',totalQuantity:''});
            fetchEvent();
        }catch(err){
            alert(err.message||'Failed to add ticket type');
        }
    };
    const handlePurchase=async (ticketTypeId) => {
        try{
            setPurchasing(ticketTypeId);
            await purchaseTicket(ticketTypeId);
            alert('Ticket purchased successfully! Check "My Tickets" to view.');
            fetchEvent();
        }catch(err){
            alert(err.message||'Failed to purchase ticket');
        }finally{
            setPurchasing(null);
        }
    };
    if(loading) return <div className="flex items-center justify-center p-16 text-slate-500 text-lg">Loading event...</div>;
    if(error) return <div className="max-w-xl mx-auto my-8 p-6 bg-red-100 text-red-800 rounded-lg text-center">Error: {error}</div>;
    if(!event) return <div className="max-w-xl mx-auto my-8 p-6 bg-red-100 text-red-800 rounded-lg text-center">Event not found</div>;

    const isOrganizer=currentRole==='ORGANIZER';
    const isAttendee=currentRole==='ATTENDEE';

    const getStatusBadgeClass=(status)=>{
        const styles={
            DRAFT: 'bg-amber-100 text-amber-800',
            PUBLISHED: 'bg-emerald-100 text-emerald-800',
            CLOSED: 'bg-red-100 text-red-800'
        };
        return styles[status] || styles.DRAFT;
    };
    return (
        <div className="p-8 max-w-4xl mx-auto">
            <button
                className="bg-transparent border-none text-indigo-500 text-base cursor-pointer py-2 mb-4 hover:underline"
                onClick={() => navigate('/')}
            >
                ← Back
            </button>
            <div className="flex justify-between items-start mb-8">
                <div className="flex items-center gap-4">
                    <h1 className="text-3xl font-bold text-white">{event.title}</h1>
                    <span className={`px-4 py-2 rounded-full text-sm font-semibold uppercase ${getStatusBadgeClass(event.status)}`}>
                        {event.status}
                    </span>
                </div>
                {isOrganizer && event.status === 'DRAFT' && (
                    <button
                        className="px-6 py-3 bg-gradient-to-br from-emerald-500 to-emerald-600 text-white rounded-lg font-semibold cursor-pointer transition-all duration-200 hover:-translate-y-px hover:shadow-lg hover:shadow-emerald-500/40"
                        onClick={handlePublish}
                    >
                        Publish Event
                    </button>
                )}
            </div>
            <div className="grid grid-cols-[repeat(auto-fill,minmax(220px,1fr))] gap-4 mb-8">
                <div className="flex items-start gap-4 bg-white p-4 rounded-lg shadow-sm">
                    <span className="text-2xl">📍</span>
                    <div>
                        <strong className="block text-slate-500 text-sm mb-1">Venue</strong>
                        <p className="text-slate-800">{event.venue}</p>
                    </div>
                </div>
                <div className="flex items-start gap-4 bg-white p-4 rounded-lg shadow-sm">
                    <span className="text-2xl">🕐</span>
                    <div>
                        <strong className="block text-slate-500 text-sm mb-1">Start Time</strong>
                        <p className="text-slate-800">{new Date(event.startTime).toLocaleString()}</p>
                    </div>
                </div>
                <div className="flex items-start gap-4 bg-white p-4 rounded-lg shadow-sm">
                    <span className="text-2xl">🕐</span>
                    <div>
                        <strong className="block text-slate-500 text-sm mb-1">End Time</strong>
                        <p className="text-slate-800">{new Date(event.endTime).toLocaleString()}</p>
                    </div>
                </div>
                <div className="flex items-start gap-4 bg-white p-4 rounded-lg shadow-sm">
                    <span className="text-2xl">👤</span>
                    <div>
                        <strong className="block text-slate-500 text-sm mb-1">Organizer</strong>
                        <p className="text-slate-800">{event.organizerEmail}</p>
                    </div>
                </div>
            </div>
            {event.description && (
                <div className="bg-white p-6 rounded-lg mb-8">
                    <h2 className="text-xl text-black font-semibold mb-4">Description</h2>
                    <p className="text-slate-600 leading-relaxed">{event.description}</p>
                </div>
            )}
            <div className="bg-white p-6 rounded-xl">
                <div className="flex justify-between items-center mb-6">
                    <h2 className="text-xl text-black font-semibold">Ticket Types</h2>
                    {isOrganizer && event.status === 'DRAFT' && (
                        <button
                            className="px-4 py-2 bg-indigo-500 text-white rounded-md font-medium cursor-pointer transition-all duration-200 hover:bg-indigo-600"
                            onClick={() => setShowTicketForm(!showTicketForm)}
                        >
                            {showTicketForm ? 'Cancel' : '+ Add Ticket Type'}
                        </button>
                    )}
                </div>
                {showTicketForm && (
                    <form className="bg-slate-50 p-4 rounded-lg mb-6" onSubmit={handleAddTicketType}>
                        <div className="grid grid-cols-[2fr_1fr_1fr] gap-2 mb-2">
                            <input
                                type="text"
                                placeholder="Ticket name (e.g., VIP, General)"
                                value={ticketFormData.name}
                                onChange={(e) => setTicketFormData({ ...ticketFormData, name: e.target.value })}
                                required
                                className="p-3 border border-slate-200 rounded-md text-black text-sm"
                            />
                            <input
                                type="number"
                                placeholder="Price"
                                step="0.01"
                                min="0"
                                value={ticketFormData.price}
                                onChange={(e) => setTicketFormData({ ...ticketFormData, price: e.target.value })}
                                required
                                className="p-3 border border-slate-200 rounded-md text-black text-sm"
                            />
                            <input
                                type="number"
                                placeholder="Quantity"
                                min="1"
                                value={ticketFormData.totalQuantity}
                                onChange={(e) => setTicketFormData({ ...ticketFormData, totalQuantity: e.target.value })}
                                required
                                className="p-3 border border-slate-200 rounded-md text-black text-sm"
                            />
                        </div>
                        <input
                            type="text"
                            placeholder="Description (optional)"
                            value={ticketFormData.description}
                            onChange={(e) => setTicketFormData({ ...ticketFormData, description: e.target.value })}
                            className="w-full p-3 border border-slate-200 rounded-md text-black text-sm mb-2"
                        />
                        <button
                            type="submit"
                            className="px-4 py-2 bg-emerald-500 text-white rounded-md font-medium cursor-pointer transition-all duration-200 hover:bg-emerald-600"
                        >
                            Add Ticket Type
                        </button>
                    </form>
                )}
                {event.ticketTypes?.length === 0 ? (
                    <p className="text-slate-500 text-center py-8">No ticket types added yet.</p>
                ) : (
                    <div className="grid grid-cols-[repeat(auto-fill,minmax(250px,1fr))] gap-4">
                        {event.ticketTypes?.map((tt) => (
                            <div key={tt.id} className="border-2 border-slate-200 rounded-lg p-5">
                                <h3 className="text-lg font-semibold text-slate-800 mb-2">{tt.name}</h3>
                                {tt.description && <p className="text-slate-500 text-sm mb-4">{tt.description}</p>}
                                <div className="flex justify-between items-center mb-4">
                                    <span className="text-2xl font-bold text-emerald-600">${tt.price}</span>
                                    <span className="text-sm text-slate-500">
                                        {tt.remainingQuantity} / {tt.totalQuantity} available
                                    </span>
                                </div>
                                {isAttendee && event.status === 'PUBLISHED' && tt.remainingQuantity > 0 && (
                                    <button
                                        className="w-full px-4 py-2 bg-indigo-500 text-white rounded-md font-medium cursor-pointer transition-all duration-200 hover:bg-indigo-600 disabled:opacity-60 disabled:cursor-not-allowed"
                                        onClick={() => handlePurchase(tt.id)}
                                        disabled={purchasing === tt.id}
                                    >
                                        {purchasing === tt.id ? 'Purchasing...' : 'Buy Ticket'}
                                    </button>
                                )}
                                {tt.remainingQuantity === 0 && (
                                    <span className="block text-center bg-red-100 text-red-800 py-2 rounded-md font-semibold">
                                        SOLD OUT
                                    </span>
                                )}
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
}

export default EventDetails;
