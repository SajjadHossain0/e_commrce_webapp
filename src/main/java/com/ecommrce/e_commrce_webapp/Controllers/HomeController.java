/*
1. home
2. categories
*/

package com.ecommrce.e_commrce_webapp.Controllers;

import com.ecommrce.e_commrce_webapp.Entities.*;
import com.ecommrce.e_commrce_webapp.Repositories.AdvertisementRepository;
import com.ecommrce.e_commrce_webapp.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {


    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SubCategoryService subCategoryService;
    @Autowired
    private AdvertisementRepository advertisementRepository;
    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String home(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        List<Advertisement> ads = advertisementRepository.findAll().stream().map(ad -> {
            String base64Image = Base64.getEncoder().encodeToString(ad.getImageData());
            ad.setBase64Image(base64Image);  // Assuming you've added a 'base64Image' field in the Advertisement entity
            return ad;
        }).collect(Collectors.toList());
        model.addAttribute("advertisements", ads);

        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);


        return "home";
    }

    /*========== category ================*/
    @GetMapping("/categories/{categoryId}")
    public String getCategory(@PathVariable Long categoryId, Model model) {

        Category category = categoryService.getCategoryById(categoryId);
        model.addAttribute("category", category);

        List<SubCategory> subcategories = (List<SubCategory>) subCategoryService.getSubCategoryById(categoryId);
        model.addAttribute("subcategories", subcategories);

        List<Product> productsByCategory = productService.getProductsByCategory(category.getName());
        model.addAttribute("productsByCategory", productsByCategory);

        return "view/view_category";
    }

    @ModelAttribute
    public void addAttributes(Principal principal, Model model) {
        //List<Category> categories = categoryService.getAllCategories();
        List<Category> categories = categoryService.getAllCategoriesWithSubCategories(); // You need to fetch categories with their subcategories
        model.addAttribute("categoryForNavbar", categories);

        List<SubCategory> subCategories = subCategoryService.getAllSubCategories();
        model.addAttribute("subCategories", subCategories);

    }
    /*========== category end ================*/


    /*========== sub category ================*/

    @GetMapping("/sub-category/{subCategoryId}")
    public String getSubCategory(@PathVariable Long subCategoryId, Model model) {
        SubCategory subCategory = subCategoryService.getSubCategoryById(subCategoryId);
        model.addAttribute("subCategory", subCategory);


        List<Product> productsBySubCategory = productService.getProductsBySubCategory(subCategory.getName());
        model.addAttribute("productsBySubCategory", productsBySubCategory);

        return "view/view_sub_category";
    }

    /*========== sub category end================*/


    @GetMapping("/product")
    public String product(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "view/view_product";
    }

    @GetMapping("/view_products/{productId}")
    public String viewProduct(@PathVariable Long productId, Model model) {
        Product product = productService.getProductById(productId);
        model.addAttribute("product", product);
        return "view/view_product";
    }


}