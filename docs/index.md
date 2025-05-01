# E-Ticaret Platformu Dokümantasyonu

Bu klasör, E-Ticaret platformu için ana dokümantasyon, tasarım ve örnek verilerini içerir.

## Dokümantasyon Yapısı

### 1. Tasarım Dökümanları
[Wireframe ve Akış Diyagramları](./wireframes/index.md)
- Ana sayfa, ürün detay sayfası ve sepet wireframe'leri
- Kullanıcı akış diyagramları
- Tasarım standartları

### 2. API Dokümantasyonu
[OpenAPI/Swagger Dokümantasyonu](./swagger/openapi.yaml)
- RESTful API endpoint tanımları
- İstek ve yanıt şemaları
- Kimlik doğrulama ve yetkilendirme açıklamaları

### 3. Veri Örnekleri
[Mock Veriler](./mock-data/index.md)
- Ürünler, kategoriler ve sepet için örnek JSON verileri
- Test ve geliştirme için kullanılabilir

## Proje Önemli Özellikler

### Müşteri İşlevleri
- Ürün arama, filtreleme ve sıralama
- Sepet ve sipariş yönetimi
- Ürün değerlendirme ve yorumlama
- Favorilere ekleme
- Sipariş takibi

### Satıcı İşlevleri
- Ürün ekleme ve düzenleme
- Varyant ve stok yönetimi
- Sipariş işleme
- Satış istatistikleri
- Mağaza profili yönetimi

### Admin İşlevleri
- Kullanıcı yönetimi
- Ürün onaylama
- Kategori yönetimi
- Site ayarları
- Raporlar ve istatistikler

## Teknoloji Yığını

### Frontend
- Angular 17.0+
- Angular Material
- NgRx Store (State Yönetimi)
- RxJS
- Angular i18n (Çoklu Dil Desteği)

### Backend
- Spring Boot 3.2+
- Spring Security (JWT Authentication)
- Spring Data JPA
- Hibernate
- MariaDB / MySQL

### Deployment
- Docker / Docker Compose
- CI/CD: GitHub Actions
- AWS / Azure 