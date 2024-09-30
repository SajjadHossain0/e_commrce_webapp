package com.ecommrce.e_commrce_webapp.Services;

import com.ecommrce.e_commrce_webapp.Entities.Category;
import com.ecommrce.e_commrce_webapp.Repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Save a new category
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    // Fetch all categories
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Optional: Get category by ID
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    // Optional: Delete category by ID
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
