package com.drinkhub.model.entity;

import com.drinkhub.model.entity.Product;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    @ManyToOne
    @JoinColumn(name="product_Id")
    private Product product;
    private int rating;
    private String comment;
}
