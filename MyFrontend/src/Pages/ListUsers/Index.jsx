import { useEffect, useState } from "react"
import { Button } from "react-bootstrap"
import { useTranslation } from "react-i18next"
import { Alert } from "../Shared/SharedComponents/Alert"
import { listUsers } from "./Api"
import userProfile from '../../assets/profile.png';
import { Link } from "react-router-dom"

export function ListUsers() {
    const [users, setUsers] = useState({
        content: [],
        number: 0,
        first: true,
        last: false
    })

    const [message, setMessage] = useState()
    const { t } = useTranslation()

    const getUsers = async (number) => {
        try {
            const response = await listUsers(number, 5)
            setUsers({
                content: response.data.content,
                number: response.data.number,
                first: response.data.first,
                last: response.data.last
            })
            if (response == null) {
                setMessage(t('tnd'))
            }
        }
        finally {
            console.log
        }
    }
    useEffect(() => {
        getUsers(0)
    }, [])

    return (
        <>
            <div className="d-flex flex-wrap gap-3 p-3">

                {/* 2. DÖNGÜ BAŞLANGICI */}
                {users.content.map((user, key) => (
                    <Link to={`/user/${user.userId}`}>
                    <div className="card" style={{ width: '18rem' }} key={user.id || key}>
                        <img
                            src={userProfile}
                            className="card-img-top"
                            alt={user.username}
                        />
                        <div className="card-body">
                            <h5 className="card-title">{user.username}</h5>
                            <h6 className="card-subtitle mb-2 text-muted">{user.mail}</h6>
                        </div>
                    </div>
                    </Link>
                ))}
            </div>
            {!users.last && <Button onClick={() => getUsers(users.number + 1)}>Next</Button>}
            {!users.first && <Button onClick={() => getUsers(users.number - 1)}>Previous</Button>}
            {message && <Alert message={message} alertType="danger" />}
        </>
    )
}