import {useState} from 'react';
import {validateTicket} from '../api/ticketApi';

function StaffDashboard(){
    const [qrCodeValue,setQrCodeValue]=useState('');
    const [result,setResult]=useState(null);
    const [loading,setLoading]=useState(false);
    const [error,setError]=useState(null);

    const handleValidate=async (e)=>{
        e.preventDefault();
        if(!qrCodeValue.trim()){
            setError('Please enter a QR code value');
            return;
        }
        try{
            setLoading(true);
            setError(null);
            setResult(null);
            const data=await validateTicket(qrCodeValue.trim());
            setResult(data);
            setQrCodeValue('');
        }catch(err){
            setError(err.message||'Validation failed');
            setResult(null);
        }finally{
            setLoading(false);
        }
    };
    const getResultStyle=()=>{
        if(!result) return {};
        return result.result==='SUCCESS'
            ?{bg:'bg-emerald-100',border:'border-emerald-500',icon:'✅'}
            :{bg:'bg-red-100',border:'border-red-500',icon:'❌'};
    };
    const resultStyle=getResultStyle();
    return(
        <div className="p-8 max-w-6xl mx-auto">
            <div className="flex justify-between items-center mb-8">
                <h1 className="text-2xl font-bold text-white">Ticket Validation</h1>
            </div>
            <div className="max-w-xl mx-auto">
                <form onSubmit={handleValidate} className="bg-white p-8 rounded-xl shadow-md mb-8">
                    <div className="mb-6">
                        <label htmlFor="qrCode" className="block font-semibold text-gray-700 mb-2">
                            Enter QR Code Value
                        </label>
                        <input
                            type="text"
                            id="qrCode"
                            value={qrCodeValue}
                            onChange={(e) => setQrCodeValue(e.target.value)}
                            placeholder="Paste or enter QR code value..."
                            className="w-full p-4 text-black border-2 border-slate-200 rounded-lg transition-colors duration-200 focus:outline-none focus:border-indigo-500 disabled:opacity-50"
                            disabled={loading}
                        />
                    </div>
                    <button
                        type="submit"
                        className="w-full px-8 py-4 bg-gradient-to-br from-indigo-500 to-violet-500 text-white rounded-lg font-semibold text-lg cursor-pointer transition-all duration-200 hover:-translate-y-px hover:shadow-lg hover:shadow-indigo-500/40 disabled:opacity-60 disabled:cursor-not-allowed disabled:hover:transform-none"
                        disabled={loading}
                    >
                        {loading ? 'Validating...' : 'Validate Ticket'}
                    </button>
                </form>
                {error&&(
                    <div className="flex items-start gap-6 p-6 rounded-xl border-2 bg-red-100 border-red-500 animate-slide-in">
                        <span className="text-4xl">❌</span>
                        <div className="flex-1">
                            <h3 className="text-xl font-semibold text-slate-800 mb-4">Validation Failed</h3>
                            <p className="text-slate-600">{error}</p>
                        </div>
                    </div>
                )}
                {result&&(
                    <div className={`flex items-start gap-6 p-6 rounded-xl border-2 animate-slide-in ${resultStyle.bg} ${resultStyle.border}`}>
                        <span className="text-4xl">{resultStyle.icon}</span>
                        <div className="flex-1">
                            <h3 className="text-xl font-semibold text-slate-800 mb-4">
                                {result.result === 'SUCCESS' ? 'Ticket Valid!' : 'Validation Failed'}
                            </h3>
                            <p className="my-2 text-slate-600"><strong className="text-slate-800">Event:</strong> {result.eventTitle}</p>
                            <p className="my-2 text-slate-600"><strong className="text-slate-800">Ticket Type:</strong> {result.ticketTypeName}</p>
                            <p className="my-2 text-slate-600"><strong className="text-slate-800">Attendee:</strong> {result.attendeeEmail}</p>
                            {result.validatedAt && (
                                <p className="my-2 text-slate-600"><strong className="text-slate-800">Validated At:</strong> {new Date(result.validatedAt).toLocaleString()}</p>
                            )}
                            <p className="mt-4 italic text-slate-600">{result.message}</p>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}

export default StaffDashboard;
