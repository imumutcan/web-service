import i18n from "i18next";
import { initReactI18next } from "react-i18next";


export const i18nInstance = i18n.use(initReactI18next)

i18nInstance.init(
    {
        resources:{
            tr:{
                translation:{
                    username:"Kullanıcı Adı",
                    password:"Şifre",
                    email:"Email",
                    passwordRepeat:"Şifre Tekrarı",
                    signup:"Üye Ol",
                    home:"Anasayfa",
                    swr:"Bir Şeyler Yanlış Gitti",
                    tnd:"Gösterilecek Veri Yok",
                    login:"Giriş Yap",
                    profile:"Profilim",
                    logout:"Çıkış Yap",
                    save:"Güncelle",
                    cancel:"İptal Et",
                    edit:"Düzenle"
                }
            },
            en:{
                translation:{
                    username:"Username",
                    password:"Password",
                    email:"Email",
                    passwordRepeat:"Password Repeat",
                    signup:"Sign Up",
                    home:"Home",
                    swr:"Something Went Wrong",
                    tnd:"There is no data to show",
                    login:"Login",
                    profile:"My Profile",
                    logout:"Logout",
                    save:"Save",
                    cancel:"Cancel",
                    edit:"Edit"
                }
            }
        },
        lng:'tr',
        fallbackLng: 'tr',
        interpolation: {
         escapeValue: false 
     }
    }
)