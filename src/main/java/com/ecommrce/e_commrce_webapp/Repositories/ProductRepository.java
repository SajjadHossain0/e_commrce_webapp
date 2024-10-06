package com.ecommrce.e_commrce_webapp.Repositories;

import com.ecommrce.e_commrce_webapp.Entities.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByUser(User user);
    List<Product> findByCategory(String category);

    // custom query to get the sub_category, here i used (_) thats why spring having issue to resolve or autowire it
    @Query("SELECT p FROM Product p WHERE p.sub_category = :subCategory")
    List<Product> findBySubCategory(@Param("subCategory") String subCategory);
}