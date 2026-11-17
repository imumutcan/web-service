import { createBrowserRouter } from "react-router-dom";
import App from '../../App'
import { Home } from "../Home/Index";
import { Activate } from "../Activate/Index";
import { SignUp } from "../SignUp/Index";
import { ListUsers } from "../ListUsers/Index"
import { User } from "../User/Index";
import { Login } from "../Login/Input";


export default createBrowserRouter(
    [
        {
            path:"/",
            Component:App,
            children:[
                {
                    path:"/",
                    Component:Home
                },
                {
                    path:"/signup",
                    Component:SignUp
                },
                {
                    path:"/activation/:token",
                    Component:Activate
                },
                {
                    path:"/list",
                    Component: ListUsers
                },
                {
                    path:"/user/:userId",
                    Component:User
                },
                {
                    path:"/login",
                    Component:Login
                }
            ]
        }
    ]
)