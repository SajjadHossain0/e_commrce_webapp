package com.ecommrce.e_commrce_webapp.Entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
public class SubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Lob
    private String coverPhoto; // This will store the image as Base64 string

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public SubCategory() {
    }

    public SubCategory(String name, String coverPhoto, Category category) {
        this.name = name;
        this.coverPhoto = coverPhoto;
        this.category = category;
    }
}
