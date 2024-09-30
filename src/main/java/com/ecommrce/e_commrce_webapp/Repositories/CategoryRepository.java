package com.ecommrce.e_commrce_webapp.Repositories;

import com.ecommrce.e_commrce_webapp.Entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
