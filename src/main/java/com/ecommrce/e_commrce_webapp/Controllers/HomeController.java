/*
1. home
2. categories
*/

package com.ecommrce.e_commrce_webapp.Controllers;

import com.ecommrce.e_commrce_webapp.Entities.Advertisement;
import com.ecommrce.e_commrce_webapp.Entities.Category;
import com.ecommrce.e_commrce_webapp.Entities.Product;
import com.ecommrce.e_commrce_webapp.Entities.SubCategory;
import com.ecommrce.e_commrce_webapp.Repositories.AdvertisementRepository;
import com.ecommrce.e_commrce_webapp.Services.CategoryService;
import com.ecommrce.e_commrce_webapp.Services.ProductService;
import com.ecommrce.e_commrce_webapp.Services.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

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
        List<SubCategory> subcategories = (List<SubCategory>) subCategoryService.getSubCategoryById(categoryId);
        model.addAttribute("category", category);
        model.addAttribute("subcategories", subcategories);
        List<Product> productsByCategory = productService.getProductsByCategory(category.getName());
        model.addAttribute("productsByCategory", productsByCategory);

        return "view/view_category";
    }
    /*========== category end ================*/


    /*========== sub category ================*/


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