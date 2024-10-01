package com.ecommrce.e_commrce_webapp.Controllers;

import com.ecommrce.e_commrce_webapp.Entities.Category;
import com.ecommrce.e_commrce_webapp.Entities.Product;
import com.ecommrce.e_commrce_webapp.Services.CategoryService;
import com.ecommrce.e_commrce_webapp.Services.ProductService;
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

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    @GetMapping("/add_product")
    public String add_product(Model model){
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        return "seller/add_product";
    }

    @PostMapping("/add-product")
    public String add_product(@RequestParam("product_image") MultipartFile product_image,
                              @RequestParam("title") String title,
                              @RequestParam("price") Double price,
                              @RequestParam("category") String category,
                              @RequestParam("quantity") int quantity,
                              @RequestParam("short_description") String short_description,
                              @RequestParam("detailed_description") String detailed_description,
                              @RequestParam("product_details") String product_details){

        Product product = new Product();
        // Convert the cover photo to a Base64 string
        String encodedImage = null;
        try {
            encodedImage = Base64.getEncoder().encodeToString(product_image.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        product.setProduct_image(encodedImage);
        product.setTitle(title);
        product.setPrice(price);
        product.setCategory(category);
        product.setQuantity(quantity);
        product.setShort_description(short_description);
        product.setDetailed_description(detailed_description);
        product.setProduct_details(product_details);

        productService.saveProduct(product);

        return "redirect:/seller/seller_dashboard";
    }


    @GetMapping("/view_orders")
    public String view_orders(){

        return "seller/view_orders";
    }


    @GetMapping("/view_products")
    public String view_products(){

        return "seller/view_products";
    }






}
