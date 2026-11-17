import { useEffect, useState } from "react"
import { useParams } from "react-router-dom"
import { useTranslation } from "react-i18next"
import { getInfo } from "./Api"
import { Alert } from "../Shared/SharedComponents/Alert"
import { UserCard } from "./Components/Card/Index"



export function User() {


    const { userId } = useParams()
    const [user, setUser] = useState("")
    const [error, setError] = useState()
    const { t } = useTranslation()


    useEffect(() => {

        const getUser = async (userId) => {
            try {
                const response = await getInfo(userId)
                setUser(response.data)
            }
            catch (error) {
                setError(error.response?.data?.message || t('tnd'))
            }
        }
        getUser(userId)
    },[])

    return <>
    <UserCard user={user}/>
        {error && <Alert message={error} alertType = "danger"/>}
    </>
}