export function Alert(props)
{
const { alertType, message } = props

    return(
       <div className={`alert alert-${alertType || "success" }`}role="alert">
        {message}
       </div>
    )
}