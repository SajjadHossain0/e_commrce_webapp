package com.ecommrce.e_commrce_webapp.Entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Order order;

    private int quantity;
    private double price; // product price at the time of order

    public OrderItem(){
    }

    public OrderItem(Long id, Product product, Order order, int quantity, double price) {
        this.id = id;
        this.product = product;
        this.order = order;
        this.quantity = quantity;
        this.price = price;
    }
}
