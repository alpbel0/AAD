# Sepet Kontrolcüsü (CartController) Test Senaryoları

## Birim Testleri

### 1. Kullanıcının Sepetini Getirme Testi
- **Test Adı:** `should_return_user_cart_when_authenticated`
- **Açıklama:** Kimlik doğrulaması yapılmış kullanıcının mevcut sepetini getirme testi
- **Başarı Kriterleri:**
  - HTTP 200 OK durum kodu dönmesi
  - CartResponse nesnesinin doğru kullanıcıya ait olması
  - Sepet içindeki ürünlerin doğru listelenmesi

### 2. Sepete Ürün Ekleme Testi
- **Test Adı:** `should_add_product_to_cart_when_valid_request`
- **Açıklama:** Ürün ve varyantsız ürün için sepete ekleme testi
- **Başarı Kriterleri:**
  - HTTP 200 OK durum kodu dönmesi
  - Sepete eklenen ürünün doğru olması
  - Ürün miktarının doğru olması

### 3. Sepete Varyantlı Ürün Ekleme Testi
- **Test Adı:** `should_add_product_with_variant_to_cart`
- **Açıklama:** Varyantlı ürün için sepete ekleme testi
- **Başarı Kriterleri:**
  - HTTP 200 OK durum kodu dönmesi
  - Sepete eklenen ürün varyantının doğru olması
  - Ürün miktarının doğru olması

### 4. Sepetten Ürün Silme Testi
- **Test Adı:** `should_remove_item_from_cart_when_valid_request`
- **Açıklama:** Sepetten belirli bir ürünü silme testi
- **Başarı Kriterleri:**
  - HTTP 200 OK durum kodu dönmesi
  - Ürünün sepetten silinmiş olması

### 5. Sepet Ürün Miktarı Güncelleme Testi
- **Test Adı:** `should_update_cart_item_quantity_when_valid_request`
- **Açıklama:** Sepetteki bir ürünün miktarını değiştirme testi
- **Başarı Kriterleri:**
  - HTTP 200 OK durum kodu dönmesi
  - Ürün miktarının güncellenmiş olması
  - Toplam fiyatın doğru hesaplanmış olması

### 6. Sepeti Tamamen Temizleme Testi
- **Test Adı:** `should_clear_cart_when_requested`
- **Açıklama:** Kullanıcının sepetini tamamen temizleme testi
- **Başarı Kriterleri:**
  - HTTP 200 OK durum kodu dönmesi
  - Sepetteki tüm ürünlerin silinmiş olması

## Entegrasyon Testleri

### 1. Sepet İşlemleri E2E Testi
- **Test Adı:** `should_perform_complete_cart_workflow`
- **Açıklama:** Sepet işlemlerinin uçtan uca testi
- **Test Adımları:**
  1. Kullanıcı girişi yap
  2. Sepeti kontrol et (boş olmalı)
  3. Sepete ürün ekle
  4. Sepeti doğrula
  5. Ürün miktarını güncelle
  6. Sepeti doğrula
  7. Sepetten ürün sil
  8. Sepeti doğrula
  9. Sepeti temizle
  10. Sepeti doğrula (boş olmalı)

### 2. Stok Kontrolü Entegrasyon Testi
- **Test Adı:** `should_check_stock_when_adding_to_cart`
- **Açıklama:** Stok kontrolü ile sepet işlemleri entegrasyon testi
- **Test Adımları:**
  1. Kullanıcı girişi yap
  2. Düşük stoklu ürün seç
  3. Maksimum stok miktarını aşan bir değeri sepete eklemeyi dene
  4. Hata yanıtını doğrula
  5. Geçerli stok miktarı içinde sepete ekleme yap
  6. Sepeti doğrula

### 3. Yetkilendirme Entegrasyon Testi
- **Test Adı:** `should_validate_authorization_for_cart_operations`
- **Açıklama:** Sepet işlemleri için yetkilendirme testi
- **Test Adımları:**
  1. Giriş yapmadan sepet işlemlerini dene
  2. 401 Unauthorized hatası doğrula
  3. Kullanıcı girişi yap
  4. Aynı işlemleri tekrar dene
  5. Başarılı yanıtları doğrula

## Performans Testleri

### 1. Eşzamanlı Sepet İşlemleri Testi
- **Test Adı:** `should_handle_concurrent_cart_operations`
- **Açıklama:** Birden fazla eşzamanlı sepet işlemi testi
- **Test Adımları:**
  1. 100 eşzamanlı sanal kullanıcı oluştur
  2. Her kullanıcı için sepet işlemleri yap (ekleme, güncelleme, silme)
  3. Yanıt sürelerini ölç
  4. Hata oranını ölç
  5. Kabul edilebilir performans kriterleri:
     - Ortalama yanıt süresi < 500ms
     - 95. yüzdelik yanıt süresi < 1500ms
     - Hata oranı < %1 