package com.ecommrce.e_commrce_webapp.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Lob
    private String coverPhoto; // This will store the image as Base64 string

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<SubCategory> subCategory;

    public Category() {
    }

    public Category(Long id, String name, String coverPhoto, List<SubCategory> subCategory) {
        this.id = id;
        this.name = name;
        this.coverPhoto = coverPhoto;
        this.subCategory = subCategory;
    }

    public Category(String name, String encodedImage) {
    }
}

