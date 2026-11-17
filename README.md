# Full-Stack User Management & Authentication Platform

Spring Boot ve React (Vite) ile geliştirilmiş; JWT tabanlı kimlik doğrulama, e-posta ile hesap aktivasyonu, çoklu dil desteği (TR/EN) ve kullanıcı profil yönetimi özelliklerini içeren full-stack web uygulaması.

---

## Özellikler

- **Kimlik Doğrulama & Güvenlik:** Spring Security, JWT (Access Token & Refresh Token) ve BCrypt şifreleme ile güvenli oturum yönetimi.
- **Kayıt & E-posta Aktivasyonu:** Kayıt sonrası SMTP üzerinden aktivasyon bağlantısı gönderimi ve token doğrulaması.
- **Kullanıcı & Profil Yönetimi:** Sayfalı (paginated) kullanıcı listeleme, profil bilgilerini güncelleme, şifre değiştirme ve sahiplik (`ownership`) kontrolü.
- **Dosya Yükleme:** Kullanıcı profil fotoğrafı yükleme, güncelleme ve silme işlemleri (`MultipartFile`).
- **Çoklu Dil Desteği (i18n):** Hem backend doğrulama/hata mesajlarında (`Messages_tr`, `Messages_en`) hem de frontend arayüzünde (`i18next`) Türkçe ve İngilizce dil desteği.
- **Merkezi Hata Yönetimi:** Özel exception sınıfları ve `GlobalExceptionHandler` ile standartlaştırılmış API hata yanıtları ve alan bazlı validasyonlar (`@UniqueUsername`, `@UniqueEmail`).

---

## Teknoloji Yığını

### Backend (`MyWebService`)
- **Dil & Çatı:** Java 17, Spring Boot
- **Veritabanı & ORM:** MySQL, Spring Data JPA (Hibernate)
- **Güvenlik:** Spring Security, JJWT (`0.12.6`)
- **Diğer Araçlar:** Spring Validation, Spring Mail, Lombok, Maven

### Frontend (`MyFrontend`)
- **Kütüphane & Yapı Aracı:** React 19, Vite
- **Durum Yönetimi & Yönlendirme:** Redux Toolkit, React Router DOM v7
- **HTTP & Çoklu Dil:** Axios, i18next (`react-i18next`)
- **Stil & UI:** Bootstrap 5, React-Bootstrap

---

## Proje Yapısı

```text
Web/
├── MyWebService/                # Spring Boot Backend
│   ├── src/main/java/.../
│   │   ├── auth/                # Giriş (Login) ve kimlik doğrulama
│   │   ├── common/              # Sayfalama (PageResponse) modelleri
│   │   ├── configuration/       # Security, CORS, Web ve uygulama ayarları
│   │   ├── email/               # E-posta gönderim servisi
│   │   ├── exception/           # Global exception handler ve özel hatalar
│   │   ├── file/                # Profil resmi yükleme/silme servisi
│   │   ├── security/            # JWT filtreleri ve UserDetails yapısı
│   │   ├── token/               # JWT ve RefreshToken servisleri
│   │   └── user/                # Kullanıcı CRUD, DTO ve özel validatörler
│   ├── src/main/resources/      # application.properties ve dil dosyaları
│   └── .env.example             # Örnek ortam değişkenleri şablonu
│
└── MyFrontend/                  # React (Vite) Frontend
    ├── src/Pages/
    │   ├── Activate/            # Hesap aktivasyon ekranı
    │   ├── Api/                 # Axios istemci ve interceptor ayarları
    │   ├── Home/                # Ana sayfa
    │   ├── ListUsers/           # Sayfalı kullanıcı listesi
    │   ├── Locales/             # TR / EN çeviri dosyaları
    │   ├── Login/               # Kullanıcı giriş ekranı
    │   ├── Router/              # Uygulama içi yönlendirmeler
    │   ├── Shared/              # Ortak bileşenler (Navbar, Input vb.) ve Redux store
    │   ├── SignUp/              # Kullanıcı kayıt ekranı
    │   └── User/                # Kullanıcı profil ve düzenleme kartları
    └── package.json
```

---

## Kurulum ve Çalıştırma

### Ön Gereksinimler
- **Java 17** veya üzeri
- **Node.js 18+** ve **npm**
- **MySQL** sunucusu

### 1. Backend Kurulumu (`MyWebService`)

1. Backend dizinine gidin ve `.env.example` dosyasını `.env` olarak kopyalayın:
   ```bash
   cd MyWebService
   cp .env.example .env
   ```

2. `.env` dosyasını kendi yerel ayarlarınıza göre düzenleyin:
   ```env
   DB_USERNAME=root
   DB_PASSWORD=your-db-password
   DB_NAME=mywebservice_db
   JWT_SECRET=openssl-rand-base64-32-ile-uretilen-anahtar
   SPRING_MAIL_USERNAME=your-ethereal-user@ethereal.email
   SPRING_MAIL_PASSWORD=your-ethereal-password
   ```

3. Uygulamayı Maven Wrapper ile başlatın:
   ```bash
   ./mvnw spring-boot:run
   ```
   Backend varsayılan olarak `http://localhost:8080` adresinde çalışacaktır.

### 2. Frontend Kurulumu (`MyFrontend`)

1. Yeni bir terminalde frontend dizinine gidin ve bağımlılıkları yükleyin:
   ```bash
   cd MyFrontend
   npm install
   ```

2. Geliştirme sunucusunu başlatın:
   ```bash
   npm run dev
   ```
   Frontend varsayılan olarak `http://localhost:5173` adresinde çalışacaktır.

---

## Temel API Uç Noktaları (Endpoints)

| Metot | Endpoint | Açıklama | Yetki |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/users` | Yeni kullanıcı kaydı oluşturur | Açık |
| `PATCH` | `/api/v1/users/{token}/activate` | E-posta token'ı ile hesabı aktifleştirir | Açık |
| `POST` | `/api/v1/users/auth` | Kullanıcı girişi yapar ve JWT döner | Açık |
| `GET` | `/api/v1/users` | Kullanıcıları sayfalı olarak listeler | Açık / Opsiyonel |
| `GET` | `/api/v1/users/{id}` | Belirtilen kullanıcının detaylarını getirir | Açık |
| `PUT` | `/api/v1/users/{id}` | Kullanıcı profil bilgilerini günceller | Kullanıcı (Sahibi) |
| `PUT` | `/api/v1/users/{id}/password` | Kullanıcı şifresini günceller | Kullanıcı (Sahibi) |
| `POST` | `/api/v1/users/{id}/image` | Profil fotoğrafı yükler (`multipart/form-data`) | Kullanıcı (Sahibi) |
| `DELETE` | `/api/v1/users/{id}/image` | Mevcut profil fotoğrafını siler | Kullanıcı (Sahibi) |
