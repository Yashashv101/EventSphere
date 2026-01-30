function TicketCard({ticket,showQR=true}){
    const formatDate=(date)=>{
        return new Date(date).toLocaleDateString("en-US",{
            weekday:'short',
            year:'numeric',
            month:'short',
            day:'numeric',
            hour:'2-digit',
            minute:'2-digit'
        });
    }
    const getStatusStyle=(status)=>{
        return status==='ACTIVE'
            ?'bg-emerald-100 text-emerald-800'
            :'bg-red-100 text-red-800';
    }
    return(
        <div className="bg-white rounded-xl p-6 shadow-md border border-slate-200">
            <div className="flex justify-between items-center mb-4 pb-4 border-b-2 border-dashed border-slate-200">
                <h3 className="text-xl font-semibold text-slate-800">{ticket.eventTitle}</h3>
                <span className={`px-3 py-1 rounded-full text-xs font-semibold uppercase ${getStatusStyle(ticket.status)}`}>
                    {ticket.status}
                </span>
            </div>
            <div className="flex gap-8">
                <div className="flex-1">
                    <p className="my-2 text-sm text-slate-600">
                        <strong className="text-slate-800">Type:</strong> {ticket.ticketTypeName}
                    </p>
                    <p className="my-2 text-sm text-slate-600">
                        <strong className="text-slate-800">Venue:</strong> {ticket.eventVenue}
                    </p>
                    <p className="my-2 text-sm text-slate-600">
                        <strong className="text-slate-800">Date:</strong> {formatDate(ticket.eventStartTime)}
                    </p>
                    <p className="my-2 text-sm text-slate-600">
                        <strong className="text-slate-800">Price:</strong> ${ticket.price}
                    </p>
                    <p className="my-2 text-sm text-slate-600">
                        <strong className="text-slate-800">Purchased:</strong> {formatDate(ticket.purchasedAt)}
                    </p>
                </div>

                {showQR && ticket.qrCodeImageData && (
                    <div className="flex flex-col items-center p-4 bg-slate-50 rounded-lg">
                        <img
                            src={`data:image/png;base64,${ticket.qrCodeImageData}`}
                            alt="Ticket QR Code"
                            className="w-[150px] h-[150px] border-2 border-slate-200 rounded-lg"
                        />
                        <p className="mt-2 font-mono text-xs text-slate-500 break-all text-center max-w-[150px]">
                            {ticket.qrCodeValue}
                        </p>
                    </div>
                )}
            </div>
        </div>
    )
}

export default TicketCard;