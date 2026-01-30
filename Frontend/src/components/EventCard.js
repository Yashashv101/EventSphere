import {Link} from 'react-router-dom';

function EventCard({event,showActions,onPublish,onClose,onAddTicketType}){
    const formatDate=(date)=>{
        return new Date(date).toLocaleDateString('en-US',{
            weekday:'short',
            year:'numeric',
            month:'short',
            day:'numeric',
            hour:'2-digit',
            minute:'2-digit',
        });
    }
    const getStatusBadge=(status)=>{
        const statusStyles={
            DRAFT:'bg-amber-100 text-amber-800',
            PUBLISHED:'bg-emerald-100 text-emerald-800',
            CLOSED:'bg-red-100 text-red-800'
        }
        return(
            <span className={`px-3 py-1 rounded-full text-xs font-semibold uppercase ${statusStyles[status] || statusStyles.DRAFT}`}>
                {status}
            </span>
        );
    }
    return (
        <div className="bg-white rounded-xl p-6 shadow-md border border-slate-200 transition-all duration-200 hover:-translate-y-0.5 hover:shadow-lg">
            <div className="flex justify-between items-start mb-4">
                <h3 className="text-xl font-semibold text-slate-800 flex-1 mr-4">{event.title}</h3>
                {getStatusBadge(event.status)}
            </div>

            <div className="flex flex-col gap-2 mb-4">
                <p className="text-sm text-slate-500">📍 {event.venue}</p>
                <p className="text-sm text-slate-500">🕐 {formatDate(event.startTime)}</p>
                {event.totalAvailableTickets !== undefined && (
                    <p className="text-sm text-slate-500">🎟️ {event.totalAvailableTickets} tickets available</p>
                )}
            </div>

            <div className="flex gap-2 flex-wrap pt-4 border-t border-slate-200">
                <Link
                    to={`/events/${event.id}`}
                    className="px-4 py-2 bg-slate-200 text-slate-600 rounded-md font-medium text-sm no-underline transition-all duration-200 hover:bg-slate-300"
                >
                    View Details
                </Link>

                {showActions && (
                    <>
                        {event.status === 'DRAFT' && onAddTicketType && (
                            <button
                                className="px-4 py-2 bg-indigo-500 text-white rounded-md font-medium text-sm cursor-pointer transition-all duration-200 hover:bg-indigo-600 hover:-translate-y-px hover:shadow-lg hover:shadow-indigo-500/40"
                                onClick={() => onAddTicketType(event.id)}
                            >
                                Add Ticket Type
                            </button>
                        )}
                        {event.status === 'DRAFT' && onPublish && (
                            <button
                                className="px-4 py-2 bg-emerald-500 text-white rounded-md font-medium text-sm cursor-pointer transition-all duration-200 hover:bg-emerald-600 hover:-translate-y-px hover:shadow-lg hover:shadow-emerald-500/40"
                                onClick={() => onPublish(event.id)}
                            >
                                Publish
                            </button>
                        )}
                        {event.status === 'PUBLISHED' && onClose && (
                            <button
                                className="px-4 py-2 bg-red-500 text-white rounded-md font-medium text-sm cursor-pointer transition-all duration-200 hover:bg-red-600 hover:-translate-y-px hover:shadow-lg hover:shadow-red-500/40"
                                onClick={() => onClose(event.id)}
                            >
                                Close Event
                            </button>
                        )}
                    </>
                )}
            </div>
        </div>
    );
}

export default EventCard;
