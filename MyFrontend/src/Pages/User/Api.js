import api from "../Api"

export function getInfo(userId)
{
    return api.get(`/users/${userId}`)
}