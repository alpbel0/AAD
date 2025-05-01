package com.example.demo.repository;

import com.example.demo.entity.Order;
import com.example.demo.entity.User;
import com.example.demo.entity.enums.OrderStatus;
import com.example.demo.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends BaseRepository<Order, Long> {
    Optional<Order> findByOrderNumber(String orderNumber);
    
    Page<Order> findByUser(User user, Pageable pageable);
    
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
    
    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.createdAt <= :deadline")
    List<Order> findExpiredOrders(OrderStatus status, LocalDateTime deadline);
    
    @Query("SELECT o FROM Order o WHERE o.user = :user AND o.status IN :statuses")
    Page<Order> findByUserAndStatusIn(User user, List<OrderStatus> statuses, Pageable pageable);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = :status AND o.createdAt >= :startDate")
    Long countByStatusAndCreatedAtAfter(OrderStatus status, LocalDateTime startDate);
}