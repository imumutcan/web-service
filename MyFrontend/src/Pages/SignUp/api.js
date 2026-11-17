import api from "../Api";


export function register(body)
{
    return api.post('/users', body);
}