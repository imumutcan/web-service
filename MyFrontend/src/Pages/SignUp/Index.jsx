import { useEffect, useMemo, useState } from "react"
import { register } from './api'
import { useTranslation } from 'react-i18next';



import { Input } from "../Shared/SharedComponents/Input"
import { Alert } from "../Shared/SharedComponents/Alert"
import { Spinner } from "../Shared/SharedComponents/Spinner";

export function SignUp() {
    const [username, setUsername] = useState()
    const [password, setPassword] = useState()
    const [email, setEmail] = useState()
    const [passwordRepeat, setPasswordRepeat] = useState()
    const [successMessage, setSuccessMessage] = useState()
    const [errors, setErorrs] = useState({})
    const [generalError, setGeneralError] = useState()
    const[apiProgress, setApiProggres] = useState(false)
    const { t } = useTranslation()


    useEffect(() => {
        setErorrs(function (lastErrors) {
            return {
                ...lastErrors,
                username: undefined,
            };
        });
    }, [username])

    useEffect(() => {
        setErorrs(function (lastErrors) {
            return {
                ...lastErrors,
                email: undefined
            };
        });
    }, [email])


    useEffect(() => {
        setErorrs(function (lastErrors) {
            return {
                ...lastErrors,
                password: undefined
            };
        });
    }, [password])

    const onSubmit = async (event) => {
        event.preventDefault();
        setGeneralError("")
        setSuccessMessage("")

        try {
            const response = await register({
                username,
                password,
                email
            });
            setSuccessMessage(response.data.message)
        }
        catch (error) {
            if (error.response?.data) {
                if (error.response.data.status == 400) {
                    setErorrs(error.response.data.errors)
                }
                else {
                    setGeneralError(error.response.data.message)
                }
            }
            else {
                setGeneralError("Generic Error")
            }
        }
        finally {
            setApiProggres(false)
        }
    }
    const passwordRepeatError = useMemo(() => {
        if (password && password !== passwordRepeat) {
            return "Password Missmatch";
        }
        return "";
    }, [password, passwordRepeat]);

    return (
        <div className="container">
            <div className="col-lg-6 offset-lg-3 col-sm-8 offset-sm-2">
                <form className="card" onSubmit={onSubmit}>
                    <div className="text-center card-header">
                        <h1>{t('signup')}</h1>
                    </div>
                    <div className="card-body">
                        <Input
                            Id="username"
                            labelText={t('username')}
                            onChange={(event) => { setUsername(event.target.value) }}
                            type="text"
                            error={errors.username}
                        />

                        <Input
                            Id="email"
                            labelText={t('email')}
                            onChange={(event) => { setEmail(event.target.value) }}
                            type="text"
                            error={errors.email}
                        />

                        <Input
                            Id="password"
                            labelText={t('password')}
                            onChange={(event) => { setPassword(event.target.value) }}
                            type="password"
                            error={errors.password}
                        />

                        <Input
                            Id="passwordRepeat"
                            labelText={t('passwordRepeat')}
                            error={passwordRepeatError}
                            onChange={(event) => { setPasswordRepeat(event.target.value) }}
                            type="password"
                        />

                        {successMessage && <Alert message={successMessage} />}
                        {generalError && <Alert message={generalError} alertType="danger" />}

                        <div className="text-center">
                            <button type="submit"
                                className="btn btn-primary" Primary
                                disabled={!password || password !== passwordRepeat}
                                onClick={() => {setApiProggres(true)}}
                            >
                                {t('signup')}
                            </button>
                        </div>
                    </div>
                   {apiProgress && <Spinner/>}
                </form>
            </div>
        </div>
    )
}