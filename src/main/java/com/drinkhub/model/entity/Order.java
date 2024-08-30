package com.drinkhub.model.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@Table(name = "customer_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "order_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> productList;

    private double totalAmount;
    private Date orderDate;
    private String status;

    public Order(Long id, User user, List<Product> productList, Double totalAmount, String status) {
        this.id = id;
        this.user = user;
        this.productList = productList;
        this.totalAmount = totalAmount;
        this.status = status;
        this.orderDate = new Date();
    }
}
