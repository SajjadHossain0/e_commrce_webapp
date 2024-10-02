package com.ecommrce.e_commrce_webapp.Services;

import com.ecommrce.e_commrce_webapp.Entities.SubCategory;
import com.ecommrce.e_commrce_webapp.Repositories.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubCategoryService {
    @Autowired
    private SubCategoryRepository subCategoryRepository;

    public SubCategory saveSubCategory(SubCategory subCategory) {
        return subCategoryRepository.save(subCategory);
    }

    public List<SubCategory> getAllSubCategories() {
        return subCategoryRepository.findAll();
    }

    public SubCategory getSubCategoryById(Long id) {
        return subCategoryRepository.findById(id).orElse(null);
    }

    public void deleteSubCategoryById(Long id) {
        subCategoryRepository.deleteById(id);
    }
}
