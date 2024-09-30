package com.ecommrce.e_commrce_webapp.Controllers;

import com.ecommrce.e_commrce_webapp.Entities.Category;
import com.ecommrce.e_commrce_webapp.Services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/seller")
public class SellerController {


    @GetMapping("/seller_dashboard")
    public String seller(){

        return "seller/seller_dashboard";
    }

    @GetMapping("/add_product")
    public String add_product(){

        return "seller/add_product";
    }

    @GetMapping("/view_orders")
    public String view_orders(){

        return "seller/view_orders";
    }

    @GetMapping("/view_products")
    public String view_products(){

        return "seller/view_products";
    }

    /*Category controllers*/

    @GetMapping("/manage_categories")
    public String manage_categories(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "seller/manage_categories";
    }



    @Autowired
    private CategoryService categoryService;

    // Get all categories
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    // Add a new category
    @PostMapping("/add")
    public String addCategory(@RequestParam("name") String name,
                                @RequestParam("coverPhoto") MultipartFile coverPhoto) throws IOException {
        // Convert the cover photo to a Base64 string
        String encodedImage = null;
        try {
            encodedImage = Base64.getEncoder().encodeToString(coverPhoto.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Create and save the new category
        Category category = new Category(name, encodedImage);
        categoryService.saveCategory(category);

        return "redirect:/seller/manage_categories";
    }

    // Optional: Delete category
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build(); // Return a 200 OK response
    }



}
