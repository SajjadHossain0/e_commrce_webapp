package com.ecommrce.e_commrce_webapp.Entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    private Date orderDate;
    private String status;
    private double totalAmount;

    public Order() {
    }

    public Order(Long id, User user, List<OrderItem> orderItems, Date orderDate, String status, double totalAmount) {
        this.id = id;
        this.user = user;
        this.orderItems = orderItems;
        this.orderDate = orderDate;
        this.status = status;
        this.totalAmount = totalAmount;
    }
}