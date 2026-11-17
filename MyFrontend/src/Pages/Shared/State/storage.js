export function storeAuthState(auth)
{
    localStorage.setItem('auth',JSON.stringify(auth))
}


export function loadAuthState()
{
    const defaultAuthstate = {
        id:0,
    };
    const authStateInLocalStorage = localStorage.getItem('auth');
    if(!authStateInLocalStorage) return defaultAuthstate;
    try{
       return  JSON.parse(authStateInLocalStorage)
    }
    catch{
        return defaultAuthstate;
    }
}