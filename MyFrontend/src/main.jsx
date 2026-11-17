import { createRoot } from 'react-dom/client'
import './index.css'
import './Pages/Locales/Translations/Index.js'
import router from './Pages/Router/Index.js'
import { RouterProvider } from 'react-router-dom'

createRoot(document.getElementById('root')).render(
  
    <RouterProvider router={router}/>
  
)
