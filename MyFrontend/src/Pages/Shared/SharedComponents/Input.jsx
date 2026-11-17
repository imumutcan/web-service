export function Input(props) {
    const { Id, labelText, error, type, onChange , defaultValue} = props

    return (
        <>
            <div className="mb-3">
                <label htmlFor={Id}>{labelText}</label>
                <input
                    id={Id}
                    type={type}
                    onChange={onChange}
                    defaultValue={defaultValue}
                    className={error ? "form-control is-invalid" : "form-control"}
                ></input>
                <div className="invalid-feedback">{error}</div>
            </div>
        </>
    )
}