import { useState, useRef } from 'react'
import defaultProfile from '../../../../assets/profile.png'
import { Button } from 'react-bootstrap'
import { useDispatch, useSelector } from 'react-redux'
import { Input } from '../../../Shared/SharedComponents/Input'
import { MButton } from '../../../Shared/SharedComponents/Button'
import { useTranslation } from 'react-i18next'
import { updateUser, updatePassword, updateImage, deleteImage } from './Api'
import { Alert } from '../../../Shared/SharedComponents/Alert'
import { userUpdateSuccess } from '../../../Shared/State/redux'

export function UserCard({ user }) {
    const storedAuthState = useSelector((store) => store.auth)
    const [editMode, setEditMode] = useState(false)
    const [passwordEditMode, setPasswordEditMode] = useState(false)
    
    const isEditButtonVisible = storedAuthState.id === user.id && !editMode && !passwordEditMode
    
    const [newUsername, setNewUsername] = useState(storedAuthState.username)
    const [currentPassword, setCurrentPassword] = useState('')
    const [newPassword, setNewPassword] = useState('')
    
    const [generalError, setGeneralError] = useState()
    const [successMessage, setSuccessMessage] = useState()
    const [error, setError] = useState({})
    
    const dispatch = useDispatch()
    const { t } = useTranslation()
    const fileInputRef = useRef()

    const visibleUsername = storedAuthState.id === user.id ? storedAuthState.username : user.username;
    // Assume user.image is just the filename, we build the full url:
    const profileImage = (storedAuthState.id === user.id ? storedAuthState.image : user.image)
                         ? `/api/v1/assets/profile/${storedAuthState.id === user.id ? storedAuthState.image : user.image}`
                         : defaultProfile;

    const resetErrors = () => {
        setError({})
        setGeneralError()
        setSuccessMessage()
    }

    const onClickCancelButton = () => {
        setNewUsername(storedAuthState.username)
        setEditMode(false)
        resetErrors()
    }

    const onClickCancelPasswordButton = () => {
        setCurrentPassword('')
        setNewPassword('')
        setPasswordEditMode(false)
        resetErrors()
    }

    const onImageChange = async (event) => {
        if (event.target.files.length < 1) return;
        const file = event.target.files[0];
        resetErrors()
        try {
            const response = await updateImage(storedAuthState.id, file);
            // Re-dispatch login success to update image globally or add an image update action
            dispatch({ type: 'auth/loginSuccess', payload: { ...storedAuthState, image: response.data.image, token: storedAuthState.token } })
            setSuccessMessage("Profil resmi güncellendi.")
        } catch (err) {
            setGeneralError("Resim güncellenemedi.")
        }
    }

    const onImageDelete = async () => {
        resetErrors()
        try {
            await deleteImage(storedAuthState.id);
            dispatch({ type: 'auth/loginSuccess', payload: { ...storedAuthState, image: null, token: storedAuthState.token } })
            setSuccessMessage("Profil resmi silindi.")
        } catch (err) {
            setGeneralError("Resim silinemedi.")
        }
    }

    const onClickSaveButton = async () => {
        resetErrors()
        try {
            await updateUser(storedAuthState.id, { username: newUsername })
            dispatch(userUpdateSuccess(newUsername))
            setEditMode(false)
            setSuccessMessage("Kullanıcı adı güncellendi.")
        } catch (err) {
            if (err.response?.data) {
                if (err.response.data.status === 400) {
                    setError(err.response.data.errors)
                } else {
                    setGeneralError(err.response.data.message)
                }
            } else {
                setGeneralError("Generic Error")
            }
        }
    }

    const onClickSavePasswordButton = async () => {
        resetErrors()
        try {
            await updatePassword(storedAuthState.id, { currentPassword, newPassword })
            setPasswordEditMode(false)
            setCurrentPassword('')
            setNewPassword('')
            setSuccessMessage("Şifre başarıyla güncellendi.")
        } catch (err) {
            if (err.response?.data) {
                if (err.response.data.status === 400) {
                    setError(err.response.data.errors)
                } else {
                    setGeneralError(err.response.data.message)
                }
            } else {
                setGeneralError("Generic Error")
            }
        }
    }

    return (
        <div className="card">
            <div className="card-header text-center">
                <img
                    src={profileImage}
                    className="img-fluid rounded-circle shadow-sm"
                    alt={user.username}
                    width={200}
                />
                <br />
                {isEditButtonVisible && (
                    <div className="mt-2">
                        <input type="file" ref={fileInputRef} className="d-none" onChange={onImageChange} accept="image/*" />
                        <Button size="sm" onClick={() => fileInputRef.current.click()}>Resmi Değiştir</Button>{' '}
                        {(storedAuthState.id === user.id && storedAuthState.image) && (
                            <Button size="sm" variant="danger" onClick={onImageDelete}>Resmi Sil</Button>
                        )}
                    </div>
                )}
            </div>
            <div className="card-body text-center">
                {!editMode && <span className='fs-3 d-block'>{visibleUsername}</span>}
                
                {isEditButtonVisible && (
                    <div className="mt-3">
                        <Button className="me-2" onClick={() => setEditMode(true)}>{t('edit')} Username</Button>
                        <Button variant="warning" onClick={() => setPasswordEditMode(true)}>Şifre Değiştir</Button>
                    </div>
                )}

                {editMode && (
                    <div className="mt-3">
                        <Input
                            labelText={t('username')}
                            Id="username"
                            defaultValue={visibleUsername}
                            onChange={(event) => setNewUsername(event.target.value)}
                            error={error.username}
                        />
                        <div className="mt-2">
                            <MButton text={t('save')} onClick={onClickSaveButton} />
                            <div className='d-inline m-1'></div>
                            <MButton text={t('cancel')} onClick={onClickCancelButton} styleType="secondary" />
                        </div>
                    </div>
                )}

                {passwordEditMode && (
                    <div className="mt-3">
                        <Input
                            labelText="Mevcut Şifre"
                            Id="currentPassword"
                            type="password"
                            onChange={(event) => setCurrentPassword(event.target.value)}
                            error={error.currentPassword}
                        />
                        <Input
                            labelText="Yeni Şifre"
                            Id="newPassword"
                            type="password"
                            onChange={(event) => setNewPassword(event.target.value)}
                            error={error.newPassword}
                        />
                        <div className="mt-2">
                            <MButton text={t('save')} onClick={onClickSavePasswordButton} />
                            <div className='d-inline m-1'></div>
                            <MButton text={t('cancel')} onClick={onClickCancelPasswordButton} styleType="secondary" />
                        </div>
                    </div>
                )}

                {generalError && <div className="mt-2"><Alert alertType="danger" message={generalError}/></div>}
                {successMessage && <div className="mt-2"><Alert alertType="success" message={successMessage}/></div>}
            </div>
        </div>
    )
}