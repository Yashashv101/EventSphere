import axiosClient from "./axiosClient";

export const register = async (name, email, password, role) => {
    const response = await axiosClient.post('/auth/register', {
        name, email, password, role
    });
    return response.data;
};

export const login = async (email, password) => {
    const response = await axiosClient.post('/auth/login', {
        email, password
    });
    return response.data;
};
