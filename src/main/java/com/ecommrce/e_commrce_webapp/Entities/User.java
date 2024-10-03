package com.ecommrce.e_commrce_webapp.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;


@Table(name = "user_table", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
@Setter
@Getter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "First name is required.")
    private String first_name;
    @NotEmpty(message = "Last name is required.")
    private String last_name;
    @Email(message = "Please provide a valid email address.")
    @NotEmpty(message = "Email is required.")
    @Column(nullable = false, unique = true)
    private String email;
    @NotEmpty(message = "Password is required.")
    private String password;

    @NotEmpty(message = "Address is required.")
    private String address;
    @NotEmpty(message = "City is required.")
    private String city;
    @NotEmpty(message = "State is required.")
    private String state;
    @NotEmpty(message = "Zip Code is required.")
    private String zip;
    @NotEmpty(message = "Country is required.")
    private String country;
    @NotEmpty(message = "Phone Number is required.")
    private String phone_number;

    private String role;
    private boolean active = true;
    private String lastLoginTime;
    private String registrationTime;

    public User() {
    }

    public User(Long id, String first_name, String last_name, String email, String password, String address, String city, String state, String zip, String country, String phone_number, String role, boolean active, String lastLoginTime, String registrationTime) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.country = country;
        this.phone_number = phone_number;
        this.role = role;
        this.active = active;
        this.lastLoginTime = lastLoginTime;
        this.registrationTime = registrationTime;
    }
}
