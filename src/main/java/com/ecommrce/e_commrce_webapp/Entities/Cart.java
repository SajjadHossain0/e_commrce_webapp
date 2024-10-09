package com.ecommrce.e_commrce_webapp.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    @Transient
    private double totalprice;

    public Cart() {
    }

    public Cart(Long id, User user, Product product, int quantity, double totalprice) {
        this.id = id;
        this.user = user;
        this.product = product;
        this.quantity = quantity;
        this.totalprice = totalprice;
    }
}
