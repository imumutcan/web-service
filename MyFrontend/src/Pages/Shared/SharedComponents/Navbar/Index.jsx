import { useTranslation } from "react-i18next"
import { Link } from "react-router-dom"
import { useDispatch, useSelector } from "react-redux"
import { logoutSuccess } from "../../State/redux"
export function BNavbar() {
    const { t } = useTranslation()
    const storedAuthState = useSelector((store) => store.auth)
    const isLoggedIn = Boolean(storedAuthState.id > 0)
    const dispatch = useDispatch()
    const onClickLogout = () => {
        dispatch(logoutSuccess());
    }
    const items = [
        {
            className: "nav-item",
            text: t('home'),
            link: "/",
            show: true
        },
        {
            className: "nav-item",
            text: t('signup'),
            link: "/signup",
            show: !isLoggedIn
        },
        {
            className: "nav-item",
            text: t('login'),
            link: "/login",
            show: !isLoggedIn
        },
        {
            className: "nav-item",
            text: t('profile'),
            link: `/user/${storedAuthState.id}`,
            show: isLoggedIn
        }
    ]
    return (
        <>
            <nav className="navbar navbar-expand bg-body-tertiary shadow-sm">
                <div className="container-fluid">
                    <a className="navbar-brand" href="#">Umut Fitness</a>
                    <ul className="navbar-nav">
                        {items.filter((item) => item.show)
                            .map((item, key) => (
                                <Link to={item.link}><li key={key} className={item.className}>{item.text}</li></Link>
                            ))
                        }
                        {
                            storedAuthState.id > 0 && <li className="nav-item">
                                <span className="nav-link" role="button" onClick={onClickLogout}>
                                    {t('logout')}
                                </span>
                            </li>
                        }
                    </ul>
                </div>
            </nav>
        </>
    )
}