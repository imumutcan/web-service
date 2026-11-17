export function MButton({
    onClick,
    text,
    styleType
}) {
    return (

        <button
            onClick={onClick}
            className={`btn btn-${styleType || "primary"}`}
        >
            {text}
        </button>

    )
}