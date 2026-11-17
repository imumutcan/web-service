import api from "../Api";

export function listUsers(number, size=3)
{
    return api.get(`/users?page=${number}&size=${size}`);
}