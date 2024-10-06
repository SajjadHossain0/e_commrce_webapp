package com.ecommrce.e_commrce_webapp.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String product_image;
    @Lob
    private String product_image1;
    @Lob
    private String product_image2;
    @Lob
    private String product_image3;

    private String title;
    private double price;

    private String category;
    private String sub_category;

    private int stock;

    private String short_description;
    private String detailed_description;
    private String product_details;

    private boolean stock_available;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Product() {
    }

    public Product(Long id, String product_image, String product_image1, String product_image2, String product_image3, String title, double price, String category, String sub_category, int stock, String short_description, String detailed_description, String product_details, boolean stock_available, User user) {
        this.id = id;
        this.product_image = product_image;
        this.product_image1 = product_image1;
        this.product_image2 = product_image2;
        this.product_image3 = product_image3;
        this.title = title;
        this.price = price;
        this.category = category;
        this.sub_category = sub_category;
        this.stock = stock;
        this.short_description = short_description;
        this.detailed_description = detailed_description;
        this.product_details = product_details;
        this.stock_available = stock_available;
        this.user = user;
    }
}
