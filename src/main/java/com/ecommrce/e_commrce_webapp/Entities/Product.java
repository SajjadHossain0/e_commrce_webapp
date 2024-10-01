package com.ecommrce.e_commrce_webapp.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String product_image;

    private String title;
    private double price;

    private String category;
    private int quantity;

    private String short_description;
    private String detailed_description;
    private String product_details;

    public Product() {
    }

    public Product(Long id, String product_image, String title, double price, String category, int quantity, String short_description, String detailed_description, String product_details) {
        this.id = id;
        this.product_image = product_image;
        this.title = title;
        this.price = price;
        this.category = category;
        this.quantity = quantity;
        this.short_description = short_description;
        this.detailed_description = detailed_description;
        this.product_details = product_details;
    }
}
