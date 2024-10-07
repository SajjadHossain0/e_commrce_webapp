package com.ecommrce.e_commrce_webapp.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import java.util.ArrayList;
import java.util.List;


@Table(name = "user_table", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

}
