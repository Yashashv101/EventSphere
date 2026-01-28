import axiosClient from "./axiosClient";

export const createEvent=async(event)=>{
    const response=await axiosClient.post('/events',event);
    return response.data;
}
export const publishEvent=async(eventId)=>{
    const response=await axiosClient.post(`/events/${eventId}/publish`);
    return response.data;
}
export const deleteEvent=async(eventId)=>{
    const response=await axiosClient.post(`/events/${eventId}/close`);
    return response.data;
}
export const addTicketType=async(eventId,ticketType)=>{
    const response=await axiosClient.post(`/events/${eventId}/ticket-types`,ticketType);
    return response.data;
}
export const getPublishedEvent=async()=>{
    const response=await axiosClient.get(`/events`);
    return response.data;
}
export const getEventDetails=async(eventId)=>{
    const response=await axiosClient.get(`/events/${eventId}`);
    return response.data;
}
export const getMyEvents=async()=>{
    const response=await axiosClient.get('/events/my-events');
    return response.data;
}