package com.ecommrce.e_commrce_webapp.Repositories;

import com.ecommrce.e_commrce_webapp.Entities.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
}