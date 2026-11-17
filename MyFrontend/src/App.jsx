import { Outlet } from 'react-router-dom'
import { LanguageSelector } from './Pages/Shared/SharedComponents/LanguageSelector'
import { BNavbar } from './Pages/Shared/SharedComponents/Navbar/Index'
import { Provider } from 'react-redux'
import { store } from './Pages/Shared/State/redux'

function App() {


  return (
  
    <Provider store={store}>
    <div className="container mt-3">
      <BNavbar/>
      <Outlet/>
       <LanguageSelector/>
    </div>
    </Provider>
  )
}

export default App
