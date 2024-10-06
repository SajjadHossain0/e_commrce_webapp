package com.ecommrce.e_commrce_webapp.Repositories;

import com.ecommrce.e_commrce_webapp.Entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByUser(User user);
    List<Product> findByCategory(String category);


}