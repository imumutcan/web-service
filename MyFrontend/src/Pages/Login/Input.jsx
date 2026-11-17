import { useEffect, useState } from "react";
import { Input } from "../Shared/SharedComponents/Input";
import { useTranslation } from "react-i18next";
import { Alert } from "../Shared/SharedComponents/Alert";
import { loginUser } from "./Api";
import { Spinner } from "../Shared/SharedComponents/Spinner";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { loginSuccess } from "../Shared/State/redux";

export function Login()
{
    const{ t } = useTranslation()
    const[email, setEmail] = useState()
    const[password, setPassword] = useState()
    const[error, setError] = useState({})
    const[generalError, setGeneralError] = useState()
    const[apiProggres, setApiProggres] = useState()
    const[successMessage, setSuccessMessage] = useState()
    const navigate = useNavigate()
    const dispatch = useDispatch()
    const onSubmit = async(event) =>{
        setError({})
        setGeneralError("")
        setApiProggres(true)
        event.preventDefault()

        try{
            const response = await loginUser(email, password)
            setSuccessMessage(response.data.message)
            dispatch(loginSuccess(response.data))
            navigate("/")
        }
        catch(error)
        {
            if(error.response?.data)
            {
                if(error.response.data.status==400)
                {
                    setError(error.response.data.errors || {})
                }
                else
                {
                    setGeneralError(error.response.data.message)
                }
            }
            else
            {
                setGeneralError("Generic Error")
            }
        }
        finally
        {
            setApiProggres(false)
        }
    }

    useEffect(()=>{
        setError(function(lastErrors){
            return{
                ...lastErrors,
                email:undefined
            }
        })
    },[email])

    useEffect(() =>{
        setError(function(lastErrors){
            return{
                ...lastErrors,
                password:undefined
            }
        })
    },[password])


    
    return (
        <>
        <div className="container">
                    <div className="col-lg-6 offset-lg-3 col-sm-8 offset-sm-2">
                        <form className="card" onSubmit = {onSubmit}>
                            <div className="text-center card-header">
                                <h1>{t('login')}</h1>
                            </div>
                            <div className="card-body">
                                <Input
                                    Id="email"
                                    labelText={t('email')}
                                    onChange={(event) => {setEmail(event.target.value)}}
                                    type="text"
                                    error={error.email}
                                />
        
                                <Input
                                    Id="password"
                                    labelText={t('password')}
                                    onChange={(event) => {setPassword(event.target.value)}}
                                    type="password"
                                    error={error.password}
                                />
                                {successMessage && <Alert  message={successMessage}/>}
                                {generalError && <Alert message={generalError} alertType="danger"/>}
        
                                <div className="text-center">
                                    <button type="submit"
                                        className="btn btn-primary" Primary
                                        disabled={!email || !password}
                                        onClick={() => {setApiProggres(true)}}
                                    >
                                        {t('login')}
                                    </button>
                                </div>
                            </div>
                          {apiProggres && <Spinner/>}
                        </form>
                    </div>
                </div>
        </>
    )
}