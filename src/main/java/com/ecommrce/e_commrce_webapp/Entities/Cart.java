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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    private double totalPrice = 0.0; // Default value for totalPrice

    public Cart() {
    }

    public Cart(Long id, User user, List<CartItem> items, double totalPrice) {
        this.id = id;
        this.user = user;
        this.items = items;
        this.totalPrice = totalPrice;
    }
}
