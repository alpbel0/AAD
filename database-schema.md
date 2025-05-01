# E-Ticaret Platformu Veritabanı Şeması

## Tablolar ve İlişkiler

### 1. Users (Kullanıcılar)
```sql
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone VARCHAR(20),
    date_of_birth DATE,
    profile_picture VARCHAR(255),
    user_type ENUM('CUSTOMER', 'SELLER', 'ADMIN') NOT NULL DEFAULT 'CUSTOMER',
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_login_at TIMESTAMP NULL
);
```

### 2. Addresses (Adresler)
```sql
CREATE TABLE addresses (
    address_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    address_type ENUM('BILLING', 'SHIPPING') NOT NULL,
    recipient_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    country VARCHAR(50) NOT NULL DEFAULT 'Türkiye',
    city VARCHAR(50) NOT NULL,
    district VARCHAR(50) NOT NULL,
    postal_code VARCHAR(20),
    address_line1 VARCHAR(255) NOT NULL,
    address_line2 VARCHAR(255),
    is_default BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
```

### 3. Categories (Kategoriler)
```sql
CREATE TABLE categories (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    parent_category_id INT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    image_url VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_category_id) REFERENCES categories(category_id)
);
```

### 4. Sellers (Satıcılar)
```sql
CREATE TABLE sellers (
    seller_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL UNIQUE,
    store_name VARCHAR(100) NOT NULL,
    store_description TEXT,
    logo_url VARCHAR(255),
    banner_url VARCHAR(255),
    tax_id VARCHAR(50) NOT NULL,
    company_name VARCHAR(100) NOT NULL,
    company_address TEXT NOT NULL,
    contact_email VARCHAR(100) NOT NULL,
    contact_phone VARCHAR(20) NOT NULL,
    commission_rate DECIMAL(5,2) NOT NULL DEFAULT 5.00,
    is_approved BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    rating DECIMAL(3,2) DEFAULT 0.00,
    total_reviews INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
```

### 5. Products (Ürünler)
```sql
CREATE TABLE products (
    product_id INT PRIMARY KEY AUTO_INCREMENT,
    seller_id INT NOT NULL,
    category_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    discount_price DECIMAL(10,2),
    quantity INT NOT NULL DEFAULT 0,
    sku VARCHAR(50),
    barcode VARCHAR(50),
    is_featured BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    is_approved BOOLEAN DEFAULT FALSE,
    weight DECIMAL(8,2),
    dimensions VARCHAR(50),
    rating DECIMAL(3,2) DEFAULT 0.00,
    total_reviews INT DEFAULT 0,
    total_sales INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (seller_id) REFERENCES sellers(seller_id),
    FOREIGN KEY (category_id) REFERENCES categories(category_id)
);
```

### 6. Product_Images (Ürün Görselleri)
```sql
CREATE TABLE product_images (
    image_id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    is_primary BOOLEAN DEFAULT FALSE,
    display_order INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);
```

### 7. Product_Attributes (Ürün Özellikleri)
```sql
CREATE TABLE product_attributes (
    attribute_id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT NOT NULL,
    attribute_name VARCHAR(50) NOT NULL,
    attribute_value VARCHAR(255) NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);
```

### 8. Product_Variants (Ürün Varyantları)
```sql
CREATE TABLE product_variants (
    variant_id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT NOT NULL,
    variant_name VARCHAR(100) NOT NULL,
    additional_price DECIMAL(10,2) DEFAULT 0.00,
    quantity INT NOT NULL DEFAULT 0,
    sku VARCHAR(50),
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);
```

### 9. Product_Reviews (Ürün Değerlendirmeleri)
```sql
CREATE TABLE product_reviews (
    review_id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT NOT NULL,
    user_id INT NOT NULL,
    order_id INT NOT NULL,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    is_approved BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(product_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
```

### 10. Carts (Sepetler)
```sql
CREATE TABLE carts (
    cart_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
```

### 11. Cart_Items (Sepet Ürünleri)
```sql
CREATE TABLE cart_items (
    cart_item_id INT PRIMARY KEY AUTO_INCREMENT,
    cart_id INT NOT NULL,
    product_id INT NOT NULL,
    variant_id INT,
    quantity INT NOT NULL DEFAULT 1,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cart_id) REFERENCES carts(cart_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id),
    FOREIGN KEY (variant_id) REFERENCES product_variants(variant_id)
);
```

### 12. Wishlists (Favori Listeleri)
```sql
CREATE TABLE wishlists (
    wishlist_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    name VARCHAR(100) NOT NULL DEFAULT 'Favorilerim',
    is_public BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
```

### 13. Wishlist_Items (Favori Ürünleri)
```sql
CREATE TABLE wishlist_items (
    wishlist_item_id INT PRIMARY KEY AUTO_INCREMENT,
    wishlist_id INT NOT NULL,
    product_id INT NOT NULL,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (wishlist_id) REFERENCES wishlists(wishlist_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);
```

### 14. Orders (Siparişler)
```sql
CREATE TABLE orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    billing_address_id INT NOT NULL,
    shipping_address_id INT NOT NULL,
    order_number VARCHAR(50) NOT NULL UNIQUE,
    order_status ENUM('PENDING', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED', 'REFUNDED') NOT NULL DEFAULT 'PENDING',
    payment_method ENUM('CREDIT_CARD', 'EFT', 'CASH_ON_DELIVERY') NOT NULL,
    payment_status ENUM('PENDING', 'COMPLETED', 'FAILED', 'REFUNDED') NOT NULL DEFAULT 'PENDING',
    subtotal DECIMAL(10,2) NOT NULL,
    shipping_cost DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    tax_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    discount_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    total_amount DECIMAL(10,2) NOT NULL,
    coupon_code VARCHAR(50),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (billing_address_id) REFERENCES addresses(address_id),
    FOREIGN KEY (shipping_address_id) REFERENCES addresses(address_id)
);
```

### 15. Order_Items (Sipariş Kalemleri)
```sql
CREATE TABLE order_items (
    order_item_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    seller_id INT NOT NULL,
    variant_id INT,
    product_name VARCHAR(255) NOT NULL,
    product_sku VARCHAR(50),
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    discount_amount DECIMAL(10,2) DEFAULT 0.00,
    tax_amount DECIMAL(10,2) DEFAULT 0.00,
    total_price DECIMAL(10,2) NOT NULL,
    status ENUM('PENDING', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED', 'REFUNDED') NOT NULL DEFAULT 'PENDING',
    is_reviewed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id),
    FOREIGN KEY (seller_id) REFERENCES sellers(seller_id),
    FOREIGN KEY (variant_id) REFERENCES product_variants(variant_id)
);
```

### 16. Shipments (Sevkiyatlar)
```sql
CREATE TABLE shipments (
    shipment_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    order_item_id INT,
    carrier_name VARCHAR(100) NOT NULL,
    tracking_number VARCHAR(100),
    shipping_date TIMESTAMP NULL,
    estimated_delivery_date TIMESTAMP NULL,
    actual_delivery_date TIMESTAMP NULL,
    status ENUM('PROCESSING', 'SHIPPED', 'IN_TRANSIT', 'DELIVERED', 'FAILED') NOT NULL DEFAULT 'PROCESSING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (order_item_id) REFERENCES order_items(order_item_id)
);
```

### 17. Returns (İadeler)
```sql
CREATE TABLE returns (
    return_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    order_item_id INT NOT NULL,
    user_id INT NOT NULL,
    return_reason ENUM('DAMAGED', 'WRONG_ITEM', 'DEFECTIVE', 'OTHER') NOT NULL,
    return_description TEXT,
    status ENUM('REQUESTED', 'APPROVED', 'DECLINED', 'RECEIVED', 'REFUNDED') NOT NULL DEFAULT 'REQUESTED',
    refund_amount DECIMAL(10,2),
    refund_method ENUM('ORIGINAL_PAYMENT', 'STORE_CREDIT') NOT NULL DEFAULT 'ORIGINAL_PAYMENT',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (order_item_id) REFERENCES order_items(order_item_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
```

### 18. Coupons (Kuponlar)
```sql
CREATE TABLE coupons (
    coupon_id INT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    discount_type ENUM('PERCENTAGE', 'FIXED_AMOUNT') NOT NULL,
    discount_value DECIMAL(10,2) NOT NULL,
    minimum_purchase DECIMAL(10,2) DEFAULT 0.00,
    maximum_discount DECIMAL(10,2),
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    usage_limit INT,
    usage_count INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 19. Payments (Ödemeler)
```sql
CREATE TABLE payments (
    payment_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    payment_method ENUM('CREDIT_CARD', 'EFT', 'CASH_ON_DELIVERY') NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    status ENUM('PENDING', 'COMPLETED', 'FAILED', 'REFUNDED') NOT NULL DEFAULT 'PENDING',
    transaction_id VARCHAR(100),
    payment_date TIMESTAMP NULL,
    card_last_four VARCHAR(4),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
);
```

### 20. User_Activities (Kullanıcı Aktiviteleri)
```sql
CREATE TABLE user_activities (
    activity_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    activity_type ENUM('LOGIN', 'LOGOUT', 'REGISTER', 'PASSWORD_CHANGE', 'ADDRESS_ADD', 'ORDER_PLACE', 'PRODUCT_REVIEW') NOT NULL,
    ip_address VARCHAR(50),
    user_agent VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
```

## Veritabanı İlişki Diyagramı

```
                          +----------------+
                          |     Users      |
                          +----------------+
                          | user_id        |
                          | email          |
                          | password       |
                          | first_name     |
                          | ...            |
                          +----------------+
                            |     |     |
            +---------------+     |     +----------------+
            |                     |                      |
  +---------v----------+   +------v--------+   +---------v----------+
  |     Addresses      |   |    Sellers    |   |    UserActivities  |
  +--------------------+   +---------------+   +--------------------+
  | address_id         |   | seller_id     |   | activity_id        |
  | user_id            |   | user_id       |   | user_id            |
  | address_type       |   | store_name    |   | activity_type      |
  | ...                |   | ...           |   | ...                |
  +--------------------+   +---------------+   +--------------------+
           |                      |
           |                      |
  +--------v---------+    +-------v------+
  |     Orders       |    |   Products   |
  +------------------+    +--------------+
  | order_id         |    | product_id   |
  | user_id          |    | seller_id    |
  | ...              |    | category_id  |
  +------------------+    | ...          |
           |              +--------------+
           |                    |
  +--------v---------+          |
  |   Order_Items    |<---------+
  +------------------+
  | order_item_id    |
  | order_id         |
  | product_id       |
  | ...              |
  +------------------+
           |
  +--------v---------+
  |    Shipments     |
  +------------------+
  | shipment_id      |
  | order_id         |
  | order_item_id    |
  | ...              |
  +------------------+
```

## Veritabanı İndeksleri

Aşağıdaki indeksler, sorgu performansını artırmak için eklenmelidir:

```sql
-- Users tablosu için indeksler
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_user_type ON users(user_type);

-- Products tablosu için indeksler
CREATE INDEX idx_products_seller_id ON products(seller_id);
CREATE INDEX idx_products_category_id ON products(category_id);
CREATE INDEX idx_products_name ON products(name);
CREATE INDEX idx_products_price ON products(price);
CREATE INDEX idx_products_is_active ON products(is_active);

-- Orders tablosu için indeksler
CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_order_number ON orders(order_number);
CREATE INDEX idx_orders_order_status ON orders(order_status);
CREATE INDEX idx_orders_created_at ON orders(created_at);

-- Order_Items tablosu için indeksler
CREATE INDEX idx_order_items_order_id ON order_items(order_id);
CREATE INDEX idx_order_items_product_id ON order_items(product_id);
CREATE INDEX idx_order_items_seller_id ON order_items(seller_id);
```