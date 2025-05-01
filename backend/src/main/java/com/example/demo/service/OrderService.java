package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.entity.enums.OrderStatus;
import com.example.demo.repository.*;
import com.example.demo.service.base.BaseService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService extends BaseService<Order, Long> {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            UserRepository userRepository,
            ProductRepository productRepository,
            ProductVariantRepository productVariantRepository) {
        super(orderRepository);
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.productVariantRepository = productVariantRepository;
    }

    public Optional<Order> findByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }

    public Page<Order> findByUserId(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        return orderRepository.findByUser(user, pageable);
    }

    public Page<Order> findByStatus(OrderStatus status, Pageable pageable) {
        return orderRepository.findByStatus(status, pageable);
    }

    public Page<Order> findByUserIdAndStatusList(Long userId, List<OrderStatus> statuses, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        return orderRepository.findByUserAndStatusIn(user, statuses, pageable);
    }

    public List<Order> findExpiredPendingOrders(int minutesThreshold) {
        LocalDateTime deadline = LocalDateTime.now().minusMinutes(minutesThreshold);
        return orderRepository.findExpiredOrders(OrderStatus.PENDING, deadline);
    }

    @Transactional
    public Order createOrder(Long userId, List<OrderItem> items, OrderAddress orderAddress, 
                            BigDecimal shippingCost, String couponCode, BigDecimal couponDiscount, String note) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // Ürün ve stok kontrolü
        validateOrderItems(items);

        // Sipariş numarası oluştur
        String orderNumber = generateOrderNumber();

        // Sipariş toplamını hesapla
        BigDecimal totalAmount = calculateTotalAmount(items);
        
        // İndirim hesapla
        BigDecimal discountAmount = couponDiscount != null ? couponDiscount : BigDecimal.ZERO;
        
        // Final tutar hesapla
        BigDecimal finalAmount = totalAmount.subtract(discountAmount).add(shippingCost);

        // Sipariş oluştur
        Order order = Order.builder()
                .user(user)
                .orderNumber(orderNumber)
                .status(OrderStatus.PENDING)
                .totalAmount(totalAmount)
                .discountAmount(discountAmount)
                .finalAmount(finalAmount)
                .shippingCost(shippingCost)
                .couponCode(couponCode)
                .couponDiscount(couponDiscount)
                .note(note)
                .build();

        Order savedOrder = orderRepository.save(order);

        // Sipariş kalemleri kaydediliyor
        for (OrderItem item : items) {
            item.setOrder(savedOrder);
            order.getItems().add(item);
        }

        // Sipariş adresi kaydediliyor
        if (orderAddress != null) {
            orderAddress.setOrder(savedOrder);
            savedOrder.setOrderAddress(orderAddress);
        }

        // Stok güncelleniyor
        updateProductStock(items);

        return orderRepository.save(savedOrder);
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Sipariş bulunamadı"));
        
        OrderStatus oldStatus = order.getStatus();
        
        // Durum değişikliği kontrolü (bazı durum geçişleri geçersiz olabilir)
        validateStatusTransition(oldStatus, newStatus);
        
        // Durum değişikliğiyle ilgili zaman bilgisini güncelle
        updateStatusTimestamp(order, newStatus);
        
        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    @Transactional
    public Order cancelOrder(Long orderId, String cancellationReason) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Sipariş bulunamadı"));
        
        // Sadece belirli durumdaki siparişler iptal edilebilir
        if (order.getStatus() != OrderStatus.PENDING && 
            order.getStatus() != OrderStatus.PAID && 
            order.getStatus() != OrderStatus.PROCESSING) {
            throw new RuntimeException("Bu durumdaki sipariş iptal edilemez");
        }
        
        order.setStatus(OrderStatus.CANCELLED);
        order.setCancellationReason(cancellationReason);
        order.setCancelledAt(LocalDateTime.now());
        
        // İptal edilen siparişler için stok güncelleniyor
        restoreProductStock(order.getItems());
        
        return orderRepository.save(order);
    }

    // Sipariş numarası oluştur
    private String generateOrderNumber() {
        // Örnek format: YIL+AY+GUN+RANDOM (YYYYMMDD-XXXX)
        String datePart = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomPart = String.format("%04d", new Random().nextInt(10000));
        return datePart + "-" + randomPart;
    }

    // Sipariş kalemlerinin geçerliliğini kontrol et
    private void validateOrderItems(List<OrderItem> items) {
        for (OrderItem item : items) {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Ürün bulunamadı: " + item.getProduct().getId()));
            
            // Ürün aktif ve stokta var mı kontrol et
            if (!product.isActive()) {
                throw new RuntimeException("Ürün aktif değil: " + product.getName());
            }

            // Varyant kontrolü
            if (item.getProductVariant() != null) {
                ProductVariant variant = productVariantRepository.findById(item.getProductVariant().getId())
                        .orElseThrow(() -> new RuntimeException("Ürün varyantı bulunamadı"));
                
                if (!variant.isActive()) {
                    throw new RuntimeException("Ürün varyantı aktif değil: " + variant.getVariantName());
                }
                
                if (variant.getQuantity() < item.getQuantity()) {
                    throw new RuntimeException("Stok yetersiz. Mevcut: " + variant.getQuantity() + 
                                              ", İstenen: " + item.getQuantity());
                }
                
                // Varyant fiyat hesaplamaları
                item.setUnitPrice(product.getPrice().add(variant.getAdditionalPrice()));
                item.setProductName(product.getName() + " - " + variant.getVariantName());
                item.setVariantName(variant.getVariantName());
            } else {
                // Ana ürün stok kontrolü
                if (product.getQuantity() < item.getQuantity()) {
                    throw new RuntimeException("Stok yetersiz. Mevcut: " + product.getQuantity() + 
                                              ", İstenen: " + item.getQuantity());
                }
                
                item.setUnitPrice(product.getPrice());
                item.setProductName(product.getName());
            }
            
            // Toplam fiyat hesaplaması
            item.setTotalPrice(item.getUnitPrice().multiply(new BigDecimal(item.getQuantity())));
            
            // İndirim yok ise final fiyat aynıdır
            if (item.getDiscountAmount() == null) {
                item.setDiscountAmount(BigDecimal.ZERO);
            }
            
            item.setFinalPrice(item.getTotalPrice().subtract(item.getDiscountAmount()));
        }
    }

    // Sipariş kalemlerinden toplam tutarı hesapla
    private BigDecimal calculateTotalAmount(List<OrderItem> items) {
        return items.stream()
                .map(OrderItem::getFinalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Durum değişikliğinin geçerli olup olmadığını kontrol et
    private void validateStatusTransition(OrderStatus oldStatus, OrderStatus newStatus) {
        // Bazı durum geçişleri mantıksız olabilir, bunları kontrol et
        Map<OrderStatus, List<OrderStatus>> allowedTransitions = new HashMap<>();
        
        allowedTransitions.put(OrderStatus.PENDING, Arrays.asList(
                OrderStatus.PAID, OrderStatus.CANCELLED));
                
        allowedTransitions.put(OrderStatus.PAID, Arrays.asList(
                OrderStatus.PROCESSING, OrderStatus.CANCELLED, OrderStatus.REFUNDED));
                
        allowedTransitions.put(OrderStatus.PROCESSING, Arrays.asList(
                OrderStatus.SHIPPED, OrderStatus.PARTIALLY_SHIPPED, OrderStatus.CANCELLED));
                
        allowedTransitions.put(OrderStatus.SHIPPED, Arrays.asList(
                OrderStatus.DELIVERED));
                
        allowedTransitions.put(OrderStatus.PARTIALLY_SHIPPED, Arrays.asList(
                OrderStatus.SHIPPED, OrderStatus.DELIVERED));
                
        allowedTransitions.put(OrderStatus.DELIVERED, Arrays.asList(
                OrderStatus.REFUNDED, OrderStatus.PARTIALLY_REFUNDED));
                
        // Eğer geçerli bir geçiş değilse hata fırlat
        if (allowedTransitions.containsKey(oldStatus) && 
            !allowedTransitions.get(oldStatus).contains(newStatus)) {
            throw new RuntimeException(oldStatus + " durumundan " + newStatus + " durumuna geçiş yapılamaz");
        }
    }

    // Durum zamanlarını güncelle
    private void updateStatusTimestamp(Order order, OrderStatus newStatus) {
        switch (newStatus) {
            case PAID:
                order.setPaidAt(LocalDateTime.now());
                break;
            case PROCESSING:
                order.setProcessedAt(LocalDateTime.now());
                break;
            case SHIPPED:
            case PARTIALLY_SHIPPED:
                order.setShippedAt(LocalDateTime.now());
                break;
            case DELIVERED:
                order.setDeliveredAt(LocalDateTime.now());
                break;
            case CANCELLED:
                order.setCancelledAt(LocalDateTime.now());
                break;
        }
    }

    // Ürün stoklarını güncelle
    private void updateProductStock(List<OrderItem> items) {
        for (OrderItem item : items) {
            if (item.getProductVariant() != null) {
                ProductVariant variant = productVariantRepository.findById(item.getProductVariant().getId()).get();
                variant.setQuantity(variant.getQuantity() - item.getQuantity());
                productVariantRepository.save(variant);
            } else {
                Product product = productRepository.findById(item.getProduct().getId()).get();
                product.setQuantity(product.getQuantity() - item.getQuantity());
                product.setTotalSales(product.getTotalSales() + item.getQuantity());
                productRepository.save(product);
            }
        }
    }

    // İptal edilen siparişler için ürün stoklarını geri yükle
    private void restoreProductStock(List<OrderItem> items) {
        for (OrderItem item : items) {
            if (item.getProductVariant() != null) {
                ProductVariant variant = productVariantRepository.findById(item.getProductVariant().getId()).get();
                variant.setQuantity(variant.getQuantity() + item.getQuantity());
                productVariantRepository.save(variant);
            } else {
                Product product = productRepository.findById(item.getProduct().getId()).get();
                product.setQuantity(product.getQuantity() + item.getQuantity());
                product.setTotalSales(product.getTotalSales() - item.getQuantity());
                productRepository.save(product);
            }
        }
    }
}