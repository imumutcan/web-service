import api from "../Api";


export function activate(token)
{
    
    return api.patch(`/users/${token}/activate`)
}