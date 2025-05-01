# E-Ticaret Platformu Mock Verileri

Bu klasör, E-Ticaret platformu için hazırlanmış örnek JSON verilerini içerir. Bu veriler, geliştirme ve test aşamasında kullanılmak üzere tasarlanmıştır.

## Mevcut Dosyalar

### [products.json](./products.json)
Ürün verileri:
- Ürün temel bilgileri (id, ad, açıklama, fiyat vb.)
- Ürün varyantları
- Ürün özellikleri
- Satıcı bilgileri

### [cart.json](./cart.json)
Sepet verileri:
- Sepet özet bilgileri
- Sepetteki ürünler
- Ürün varyant bilgileri

### [categories.json](./categories.json)
Kategori verileri:
- Ana kategoriler
- Alt kategoriler
- Kategori ilişkileri

## Kullanım

Bu veriler, aşağıdaki şekillerde kullanılabilir:

1. **Geliştirme Ortamında:**
   ```javascript
   fetch('/mock-data/products.json')
     .then(response => response.json())
     .then(data => console.log(data));
   ```

2. **Mock API Servisi:**
   Bu dosyalar bir mock API servisi oluşturmak için kullanılabilir. Örneğin:
   
   ```
   GET /api/products -> products.json
   GET /api/cart -> cart.json
   GET /api/categories -> categories.json
   ```

3. **Test Senaryoları:**
   UI testleri ve entegrasyon testleri için test verisi olarak kullanılabilir. 