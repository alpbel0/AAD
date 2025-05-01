package com.example.demo.repository;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends BaseRepository<OrderItem, Long> {
    List<OrderItem> findByOrder(Order order);
    
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.user = :user AND oi.isReviewed = false")
    List<OrderItem> findUnreviewedItems(User user);
    
    @Query("SELECT oi.product, COUNT(oi) as count FROM OrderItem oi GROUP BY oi.product ORDER BY count DESC")
    List<Product> findTopSellingProducts(int limit);
}