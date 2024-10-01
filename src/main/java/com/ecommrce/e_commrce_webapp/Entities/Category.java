package com.ecommrce.e_commrce_webapp.Entities;

import jakarta.persistence.*;
import lombok.Getter;
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

    public Category() {}

    public Category(String name, String coverPhoto) {
        this.name = name;
        this.coverPhoto = coverPhoto;
    }

}

