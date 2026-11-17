import api from "../Api";
export function loginUser(email, password)
{
    return api.post('/users/auth', {
        email,
        password
    });
}