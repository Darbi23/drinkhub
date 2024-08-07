package com.drinkhub.repository;

import com.drinkhub.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Find orders by userId
    List<Order> findByUserId(Long userId);

    // Find orders by status
    List<Order> findByStatus(String status);

    // Find orders placed within a specific date range
    List<Order> findByOrderDateBetween(Date startDate, Date endDate);

    // Custom query to find orders by userId and status
    @Query("SELECT o FROM Order o WHERE o.userId = :userId AND o.status = :status")
    List<Order> findOrdersByUserAndStatus(
            @Param("userId") Long userId,
            @Param("status") String status);
}
