import axios from 'axios';

const API_BASE_URL="http://localhost:8080";
export const USERS={
    ORGANIZER:'11111111-1111-1111-1111-111111111111',
    ATTENDEE:'22222222-2222-2222-2222-222222222222',
    STAFF:'33333333-3333-3333-3333-333333333333'
}
const axiosClient=axios.create({
    baseURL:API_BASE_URL,
    headers:{
        'Content-Type':'application/json'
    }
})
let currentUserId=USERS.ORGANIZER;
let currentUserEmail='organizer@example.com';

export const setCurrentUser=(id,email)=>{
    currentUserId=id;
    currentUserEmail=email;
}
export const getCurrentUserId=()=>currentUserId;
axiosClient.interceptors.use(
    (config)=>{
        config.headers['X-User-Id']=currentUserId;
        config.headers['X-User-Email']=currentUserEmail;
        return config;
    },
    (error)=>Promise.reject(error)
)
axiosClient.interceptors.response.use(
    (response)=>response,
    (error)=>{
        const errorMsg=error.response?.data||{
            code:'NETWORK_ERROR',
            message:error.message
        }
        return Promise.reject(errorMsg);
    }
)
export default axiosClient;