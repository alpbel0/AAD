# E-Ticaret Web Sitesi PRD (Ürün Gereksinimleri Belgesi)

## Proje Genel Bakış
Bu proje, Trendyol ve HepsiBurada benzeri, tam kapsamlı bir e-ticaret platformunun geliştirilmesini amaçlamaktadır. Platform temel olarak üç farklı kullanıcı rolüne hizmet verecektir: müşteriler, satıcılar ve admin kullanıcılar.

## Kullanıcı Rolleri

### 1. Müşteri
- Kullanıcı hesabı oluşturma ve yönetme
- Ürünleri arama, filtreleme ve listeleme
- Ürün detaylarını görüntüleme ve değerlendirme
- Sepete ürün ekleme ve sipariş verme
- Sipariş durumunu takip etme
- İade ve değişim talepleri oluşturma

### 2. Satıcı
- Satıcı hesabı oluşturma ve yönetme
- Ürünleri yükleme, düzenleme ve silme
- Stok yönetimi
- Sipariş takibi ve onaylama
- İade ve değişim taleplerini yönetme
- Satış ve kazanç raporlarını görüntüleme

### 3. Admin
- Kullanıcı ve satıcı hesaplarını yönetme
- Ürün kategorilerini yönetme
- Platform genelindeki siparişleri izleme
- İade ve şikayetleri çözümleme
- Sistem ayarlarını yapılandırma
- Raporlama ve analiz gerçekleştirme

## Fonksiyonel Gereksinimler

### Kullanıcı Yönetimi
- Kayıt ve giriş sistemi (email  entegrasyonlu)
- Profil yönetimi ve güncelleme
- Adres ve ödeme bilgileri yönetimi
- Şifre sıfırlama ve hesap kurtarma

### Ürün Yönetimi
- Kategori ve alt kategori yapısı
- Ürün arama, filtreleme ve sıralama
- Ürün detay sayfaları
- Ürün değerlendirme ve yorumlama
- Favori ürünler ve listeler

### Sipariş Süreci
- Alışveriş sepeti yönetimi
- Sipariş oluşturma ve ödeme
- Farklı ödeme yöntemleri (kredi kartı, EFT, kapıda ödeme)
- Sipariş takibi
- Fatura oluşturma

### Satıcı Panel Özellikleri
- Ürün listeleme ve düzenleme
- Stok yönetimi
- Sipariş takibi ve durumu güncelleme
- Müşteri mesajlarını yönetme
- Satış istatistikleri ve raporlar

### Admin Panel Özellikleri
- Kullanıcı ve satıcı hesaplarını yönetme
- Kategori ve ürün onaylarını yönetme
- Platform geneli raporlar ve istatistikler
- İçerik yönetimi (banner, kampanya, duyuru)
- Sistem ayarları ve yapılandırma

## Teknik Gereksinimler

### Ön Yüz (Frontend)
- Angular framework
- Responsive tasarım ( masaüstü)
- Hızlı yükleme ve performans optimizasyonu
- Kolay kullanılabilir arayüz

### Arka Yüz (Backend)
- Spring Boot framework
- RESTful API mimarisi
- JWT tabanlı kimlik doğrulama
- Rol tabanlı yetkilendirme sistemi

### Veritabanı
- MySQL veritabanı
- Verimli ilişkisel model tasarımı
- Veri tutarlılığı ve bütünlüğü kontrolü

### Güvenlik
- HTTPS protokolü
- Güvenli kimlik doğrulama
- XSS ve CSRF koruması
- SQL enjeksiyon önleme

### Ölçeklenebilirlik
- Mikroservis mimarisi
- Önbellekleme stratejileri
- Yük dengeleme

## Proje Yapım Aşamaları

1. Kullanıcı ve admin kullanım senaryolarını inceleme ve fonksiyonel gereksinimleri belirleme
2. Satıcı kullanım senaryosunu gözden geçirme ve satıcı gereksinimlerini belirleme
3. Müşteri, satıcı ve admin rollerine ait kullanıcı hikâyelerini yazma
4. Her sayfa için wireframe ve akış diyagramı hazırlama
5. MySQL veritabanı şemasını oluşturma
6. Swagger/OpenAPI spec dosyasını yazma
7. Angular ön uç için mock JSON dosyaları üretme
8. Spring Boot arka uçta stub controller'lar yazma
9. Sprint planlaması yapma
10. Entegrasyon test senaryoları yazma
11. CI/CD pipeline kurulumu
12. Önbellekleme ve performans optimizasyonu
13. Güvenlik testleri ve OWASP uygulamaları
14. Konteynerleştirme
15. Logging ve monitoring kurulumu
16. Beta testi ve geri bildirim toplama

## Sprint Planı

### Sprint 1: Kimlik Doğrulama & Yetkilendirme
- Kullanıcı kayıt ve giriş sistemi
- Rol tabanlı yetkilendirme
- Profil yönetimi

### Sprint 2: Ürün Listeleme & Arama
- Kategori yapısı
- Ürün listeleme ve filtreleme
- Ürün detay sayfaları

### Sprint 3: Sepet & Ödeme Akışı
- Sepet yönetimi
- Ödeme entegrasyonu
- Sipariş oluşturma ve takibi

### Sprint 4: Satıcı Paneli
- Ürün yönetimi
- Sipariş takibi
- İade ve değişim yönetimi

### Sprint 5: Admin Paneli
- Kullanıcı/satıcı yönetimi
- Ürün ve kategori yönetimi
- Raporlama ve analiz

## Yapıldı
1. Müşteri, satıcı ve admin rollerine ait kullanıcı hikâyelerini yazma
2. MySQL veritabanı şemasını oluşturma
3. Kullanıcı ve admin kullanım senaryolarını inceleme ve fonksiyonel gereksinimleri çıkarma
4. Satıcı kullanım senaryosunu gözden geçirme ve gereksinimlerini belirleme
5. Spring Boot arka uçta stub controller'lar yazma

## Yapılacak
1. Her sayfa için wireframe ve akış diyagramı hazırlama
2. Swagger/OpenAPI spec dosyasını yazma
3. Angular ön uç için mock JSON dosyaları üretme
4. Sprint planını detaylandırma
5. Entegrasyon test senaryoları yazma
6. CI/CD pipeline kurulumu
7. Önbellekleme, lazy loading ve sorgu optimizasyonu yapılandırması
8. OWASP Top 10 kontrollerini uygulama ve güvenlik testleri
9. Dockerfile hazırlama ve konteynerleştirme
10. Logging ve monitoring kurulumu
11. Beta test ve geri bildirim toplama

## Backend Yapılacaklar (Spring Boot)

### Temel Yapı
1. Proje iskeletinin oluşturulması
   - Spring Boot projesinin kurulumu
   - Temel paket yapısının oluşturulması
   - Dependency'lerin eklenmesi

### Güvenlik
1. JWT tabanlı kimlik doğrulama sistemi
   - JWT üretimi ve doğrulama
   - Kullanıcı rol yönetimi
   - Güvenlik konfigürasyonu

### Veritabanı ve Model
1. Entity sınıflarının oluşturulması
   - User, Product, Order vb. entity'ler
   - İlişkilerin kurulması
2. Repository katmanının implementasyonu
3. Database migration scriptlerinin hazırlanması

### Servis Katmanı
1. Temel CRUD operasyonları için service sınıfları
2. İş mantığı implementasyonları
3. Validation işlemleri

### Controller Katmanı
1. REST API endpoint'lerinin oluşturulması
2. Request/Response DTO'ların hazırlanması
3. Exception handling mekanizması

### Cache Yönetimi
1. Redis entegrasyonu
2. Cache stratejilerinin belirlenmesi
3. Cache invalidation mekanizması

### Dosya Yönetimi
1. Ürün resimlerinin yüklenmesi ve depolanması
2. Dosya sistemi entegrasyonu
3. CDN entegrasyonu

### Ödeme Sistemi
1. Ödeme gateway entegrasyonu
2. İşlem güvenliği
3. Ödeme lifecycle yönetimi

### Loglama ve İzleme
1. Elasticsearch-Logstash-Kibana (ELK) stack kurulumu
2. Performance metrics toplama
3. Alert mekanizması

## Frontend Yapılacaklar (Angular)

### Temel Yapı
1. Angular projesinin oluşturulması
2. Routing yapısının kurulması
3. Shared modüllerin hazırlanması
4. State management (NGRX) kurulumu

### Kullanıcı Arayüzü
1. Responsive layout tasarımı
2. Tema ve stil dosyalarının hazırlanması
3. Shared komponentlerin geliştirilmesi
   - Header/Footer
   - Navigation
   - Form elementleri
   - Modal/Dialog
   - Toast/Alert

### Authentication
1. Login/Register sayfaları
2. JWT token yönetimi
3. Route guards
4. Rol bazlı erişim kontrolü

### Müşteri Modülü
1. Ana sayfa
   - Ürün listesi
   - Filtreleme ve arama
   - Kategori navigasyonu
2. Ürün detay sayfası
   - Ürün bilgileri
   - Yorum ve değerlendirmeler
3. Sepet yönetimi
   - Sepete ekleme/çıkarma
   - Miktar güncelleme
4. Checkout akışı
   - Adres seçimi
   - Ödeme yöntemi seçimi
5. Sipariş takibi
6. Kullanıcı profili yönetimi

### Satıcı Modülü
1. Satıcı dashboard
2. Ürün yönetimi
   - Ürün ekleme/düzenleme
   - Stok yönetimi
3. Sipariş yönetimi
4. Satış raporları

### Admin Modülü
1. Admin dashboard
2. Kullanıcı yönetimi
3. Kategori yönetimi
4. Satıcı onayları
5. Sistem ayarları

### Performans Optimizasyonu
1. Lazy loading implementasyonu
2. Image optimization
3. Bundle size optimizasyonu
4. Cache stratejileri

### Test
1. Unit testlerin yazılması
2. E2E testlerin hazırlanması
3. Test coverage raporlaması

### CI/CD
1. Build ve deployment scriptleri
2. Docker entegrasyonu
3. Nginx konfigürasyonu