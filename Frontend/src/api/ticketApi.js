import axiosClient from "./axiosClient";

export const purchaseTicket=async(ticketId)=>{
    const response=await axiosClient.post('/tickets/purchase',{ticketTypeId});
    return response.data;
}
export const getMyTickets=async()=>{
    const response=await axiosClient.get('/tickets/my-tickets');
    return response.data;
}
export const getTicketDetails=async(ticketId)=>{
    const response=await axiosClient.get(`/tickets/${ticketId}`);
    return response.data;
}
export const validateTicket=async(qrCodeValue)=>{
    const response=await axiosClient.post('/tickets/validate',{qrCodeValue});
    return response.data;
}
export const getValidationSatus=async(ticketId)=>{
    const response=await axiosClient.get(`/tickets/${ticketId}/validation-status`);
    return response.data;
}