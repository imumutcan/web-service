import api from "../../../Api";

export function updateUser(id, body)
{
    return api.put(`/users/${id}`, body);
}

export function updatePassword(id, body)
{
    return api.put(`/users/${id}/password`, body);
}

export function updateImage(id, image)
{
    const formData = new FormData();
    formData.append("image", image);
    return api.post(`/users/${id}/image`, formData, { headers: { "Content-Type": "multipart/form-data" } });
}

export function deleteImage(id)
{
    return api.delete(`/users/${id}/image`);
}