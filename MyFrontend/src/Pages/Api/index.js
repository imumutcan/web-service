import axios from "axios";
import { store } from "../Shared/State/redux";


const api = axios.create(
    {
        baseURL: "/api/v1",
        headers: {
            "Content-Type": "application/json"
        },
    }
);


api.interceptors.request.use((config) => {
    const state = store.getState();
    const authToken = state.auth.token;
    if (authToken) {
        config.headers.Authorization = `${authToken.prefix} ${authToken.token}`
    }
    return config;
},
    (error) => {
        return Promise.reject(error);
    }
)



export default api;



