package com.example.demo.entity.enums;

public enum OrderStatus {
    PENDING,            // Sipariş alındı, ödeme bekleniyor
    PAID,              // Ödeme alındı
    PROCESSING,        // Sipariş hazırlanıyor
    SHIPPED,           // Kargoya verildi
    DELIVERED,         // Teslim edildi
    CANCELLED,         // İptal edildi
    REFUNDED,          // İade edildi
    PARTIALLY_SHIPPED, // Kısmi gönderim yapıldı
    PARTIALLY_REFUNDED // Kısmi iade yapıldı
}