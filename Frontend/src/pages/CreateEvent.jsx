import {useState} from 'react';
import {useNavigate} from 'react-router-dom';
import {createEvent} from '../api/eventApi';

function CreateEvent(){
    const navigate=useNavigate();
    const [loading,setLoading]=useState(false);
    const [error,setError]=useState(null);
    const [formData,setFormData]=useState({
        title:'',
        venue:'',
        description:'',
        startTime:'',
        endTime:''
    });
    const handleChange=(e)=>{
        const {name,value}=e.target;
        setFormData(prev=>({...prev,[name]:value}));
    };
    const handleSubmit=async (e)=>{
        e.preventDefault();
        setError(null);
        const eventData={
            ...formData,
            startTime:new Date(formData.startTime).toISOString().slice(0,-1),
            endTime:new Date(formData.endTime).toISOString().slice(0,-1)
        };
        try{
            setLoading(true);
            const createdEvent=await createEvent(eventData);
            navigate(`/events/${createdEvent.id}`);
        }catch(err){
            setError(err.message||'Failed to create event');
        }finally{
            setLoading(false);
        }
    };
    return(
        <div className="p-8 max-w-3xl mx-auto">
            <div className="bg-white p-8 rounded-xl shadow-md">
                <h1 className="text-2xl font-bold text-slate-800 mb-8">Create New Event</h1>
                {error && <div className="bg-red-100 text-red-800 p-4 rounded-lg mb-6">{error}</div>}
                <form onSubmit={handleSubmit}>
                    <div className="mb-6">
                        <label htmlFor="title" className="block font-semibold text-gray-700 mb-2">
                            Event Title *
                        </label>
                        <input
                            type="text"
                            id="title"
                            name="title"
                            value={formData.title}
                            onChange={handleChange}
                            required
                            minLength={3}
                            maxLength={200}
                            placeholder="Enter event title"
                            className="w-full px-4 py-3 text-base border-2 border-slate-200 rounded-lg transition-colors duration-200 focus:outline-none focus:border-indigo-500"
                        />
                    </div>
                    <div className="mb-6">
                        <label htmlFor="venue" className="block font-semibold text-gray-700 mb-2">
                            Venue *
                        </label>
                        <input
                            type="text"
                            id="venue"
                            name="venue"
                            value={formData.venue}
                            onChange={handleChange}
                            required
                            maxLength={500}
                            placeholder="Enter venue address"
                            className="w-full px-4 py-3 text-base border-2 border-slate-200 rounded-lg transition-colors duration-200 focus:outline-none focus:border-indigo-500"
                        />
                    </div>
                    <div className="mb-6">
                        <label htmlFor="description" className="block font-semibold text-gray-700 mb-2">
                            Description
                        </label>
                        <textarea
                            id="description"
                            name="description"
                            value={formData.description}
                            onChange={handleChange}
                            maxLength={5000}
                            rows={4}
                            placeholder="Describe your event..."
                            className="w-full px-4 py-3 text-base border-2 border-slate-200 rounded-lg transition-colors duration-200 focus:outline-none focus:border-indigo-500 resize-y"
                        />
                    </div>
                    <div className="grid grid-cols-2 gap-4">
                        <div className="mb-6">
                            <label htmlFor="startTime" className="block font-semibold text-gray-700 mb-2">
                                Start Time *
                            </label>
                            <input
                                type="datetime-local"
                                id="startTime"
                                name="startTime"
                                value={formData.startTime}
                                onChange={handleChange}
                                required
                                className="w-full px-4 py-3 text-base border-2 border-slate-200 rounded-lg transition-colors duration-200 focus:outline-none focus:border-indigo-500"
                            />
                        </div>
                        <div className="mb-6">
                            <label htmlFor="endTime" className="block font-semibold text-gray-700 mb-2">
                                End Time *
                            </label>
                            <input
                                type="datetime-local"
                                id="endTime"
                                name="endTime"
                                value={formData.endTime}
                                onChange={handleChange}
                                required
                                className="w-full px-4 py-3 text-base border-2 border-slate-200 rounded-lg transition-colors duration-200 focus:outline-none focus:border-indigo-500"
                            />
                        </div>
                    </div>
                    <div className="flex gap-4 justify-end mt-8 pt-6 border-t border-slate-200">
                        <button
                            type="button"
                            className="px-6 py-3 bg-slate-200 text-slate-600 rounded-lg font-semibold cursor-pointer transition-all duration-200 hover:bg-slate-300"
                            onClick={() => navigate('/')}
                        >
                            Cancel
                        </button>
                        <button
                            type="submit"
                            className="px-6 py-3 bg-gradient-to-br from-indigo-500 to-violet-500 text-white rounded-lg font-semibold cursor-pointer transition-all duration-200 hover:-translate-y-px hover:shadow-lg hover:shadow-indigo-500/40 disabled:opacity-60 disabled:cursor-not-allowed"
                            disabled={loading}
                        >
                            {loading ? 'Creating...' : 'Create Event'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}

export default CreateEvent;
