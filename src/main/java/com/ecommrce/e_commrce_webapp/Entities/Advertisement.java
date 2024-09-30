package com.ecommrce.e_commrce_webapp.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private byte[] imageData;

    private LocalDateTime uploadDate;

    @Transient
    private String base64Image;  // Field to hold the Base64 string for the image

    public Advertisement() {
    }

    public Advertisement(Long id, byte[] imageData, LocalDateTime uploadDate, String base64Image) {
        this.id = id;
        this.imageData = imageData;
        this.uploadDate = uploadDate;
        this.base64Image = base64Image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }
}
