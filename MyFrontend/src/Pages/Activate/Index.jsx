import { useEffect, useState } from "react"
import { activate } from "./Api"
import { useTranslation } from "react-i18next"
import { Alert } from "../Shared/SharedComponents/Alert"
import { useParams } from "react-router-dom"

export function Activate() {
    const { token } = useParams()

    const [successMessage, setSuccessMessage] = useState()
    const [error, setError] = useState()
    const { t } = useTranslation()



    useEffect(() => {
        const active = async () => {

            try {
                const response = await activate(token)
                setSuccessMessage(response.data.message)
            }
            catch (error) {
                setError(error.response?.data?.message || t('swr'))
            }
            finally {
            }
        }
        active();
    },[])
    return (
        <>
           {successMessage && <Alert message={successMessage}/>}
           {error && <Alert message={error} alertType="danger"/>}
        </>
    )
}