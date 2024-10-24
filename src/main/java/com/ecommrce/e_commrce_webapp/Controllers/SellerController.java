package com.ecommrce.e_commrce_webapp.Controllers;

import com.ecommrce.e_commrce_webapp.Entities.*;
import com.ecommrce.e_commrce_webapp.Services.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/seller")
public class SellerController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SubCategoryService subCategoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserDataService userDataService;
    @Autowired
    private CartService cartService;


    @GetMapping("/seller_dashboard")
    public String seller(){

        return "seller/seller_dashboard";
    }

    @GetMapping("/add_product")
    public String add_product(Model model){
        List<Category> categories = categoryService.getAllCategories();
        List<SubCategory> subCategories = subCategoryService.getAllSubCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("subCategories", subCategories);

        return "seller/add_product";
    }

    @PostMapping("/add-product")
    public String add_product(@RequestParam("product_image") MultipartFile product_image, @RequestParam("product_image1") MultipartFile product_image1, @RequestParam("product_image2") MultipartFile product_image2, @RequestParam("product_image3") MultipartFile product_image3,
                              @RequestParam("title") String title, @RequestParam("price") Double price,
                              @RequestParam("category") String category, @RequestParam("sub_category") String sub_category,
                              @RequestParam("stock") int stock, @RequestParam("short_description") String short_description, @RequestParam("detailed_description") String detailed_description,
                              @RequestParam("product_details") String product_details,@RequestParam("stock_available") boolean stock_available,HttpSession session,Model model){

        Product product = new Product();
        // Convert the cover photo to a Base64 string
        String encodedImage = null;
        String encodedImage1 = null;
        String encodedImage2 = null;
        String encodedImage3 = null;
        try {
            encodedImage = Base64.getEncoder().encodeToString(product_image.getBytes());
            encodedImage1 = Base64.getEncoder().encodeToString(product_image1.getBytes());
            encodedImage2 = Base64.getEncoder().encodeToString(product_image2.getBytes());
            encodedImage3 = Base64.getEncoder().encodeToString(product_image3.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        product.setProduct_image(encodedImage);
        product.setProduct_image1(encodedImage1);
        product.setProduct_image2(encodedImage2);
        product.setProduct_image3(encodedImage3);
        product.setTitle(title);
        product.setPrice(price);
        product.setCategory(category);
        product.setSub_category(sub_category);
        product.setStock(stock);
        product.setShort_description(short_description);
        product.setDetailed_description(detailed_description);
        product.setProduct_details(product_details);
        product.setStock_available(stock_available);


        session.setAttribute("success","Product added successfully");

        productService.saveProduct(product);

        return "redirect:/seller/add_product";
    }

    @PostMapping("/edit-product")
    public String edit_product(@RequestParam("id") Long id,@RequestParam("title") String title, @RequestParam("price") Double price, @RequestParam("category") String category,
                              @RequestParam("sub_category") String sub_category, @RequestParam("stock") int stock, @RequestParam("short_description") String short_description, @RequestParam("detailed_description") String detailed_description,
                              @RequestParam("product_details") String product_details,@RequestParam("stock_available") boolean stock_available){

        Product product = productService.getProductById(id);

        product.setTitle(title);
        product.setPrice(price);
        product.setCategory(category);
        product.setSub_category(sub_category);
        product.setStock(stock);
        product.setShort_description(short_description);
        product.setDetailed_description(detailed_description);
        product.setProduct_details(product_details);
        product.setStock_available(stock_available);


        productService.saveProduct(product);

        return "redirect:/seller/view_products";
    }

    @PostMapping("/edit-product-img")
    public String edit_product_img(@RequestParam("id") Long id,@RequestParam("product_image") MultipartFile product_image,
                                   @RequestParam("product_image1") MultipartFile product_image1, @RequestParam("product_image2") MultipartFile product_image2,
                                   @RequestParam("product_image3") MultipartFile product_image3){
        Product product = productService.getProductById(id);
// Convert the cover photo to a Base64 string
        String encodedImage = null;
        String encodedImage1 = null;
        String encodedImage2 = null;
        String encodedImage3 = null;
        try {
            encodedImage = Base64.getEncoder().encodeToString(product_image.getBytes());
            encodedImage1 = Base64.getEncoder().encodeToString(product_image1.getBytes());
            encodedImage2 = Base64.getEncoder().encodeToString(product_image2.getBytes());
            encodedImage3 = Base64.getEncoder().encodeToString(product_image3.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        product.setProduct_image(encodedImage);
        product.setProduct_image1(encodedImage1);
        product.setProduct_image2(encodedImage2);
        product.setProduct_image3(encodedImage3);

        productService.saveProduct(product);

        return "redirect:/seller/view_products";

    }

    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/seller/view_products";
    }

    @GetMapping("/view_orders")
    public String view_orders(){

        return "seller/view_orders";
    }

    @GetMapping("/view_products")
    public String view_products(@AuthenticationPrincipal User user, Model model){
        List<Product> products = productService.getProductsByUser(user);
        model.addAttribute("products", products);

        List<Category> categories = categoryService.getAllCategories();
        List<SubCategory> subCategories = subCategoryService.getAllSubCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("subCategories", subCategories);

        return "seller/view_products";
    }





    @ModelAttribute
    public void addAttributes(Principal principal, Model model) {
        //List<Category> categories = categoryService.getAllCategories();
        List<Category> categories = categoryService.getAllCategoriesWithSubCategories(); // You need to fetch categories with their subcategories
        model.addAttribute("categoryForNavbar", categories);

        List<SubCategory> subCategories = subCategoryService.getAllSubCategories();
        model.addAttribute("subCategories", subCategories);

    }
    @ModelAttribute
    public void addAttributeForCart(Principal principal, Model model) {
        if (principal != null) {
            String email = principal.getName();
            User user = userDataService.getUserByEmail(email);

            // Fetch the cart items for the user
            List<Cart> cartItems = cartService.getCartItemsByUserId(user.getId());
            model.addAttribute("cartItems", cartItems);
        }
    }
    @ModelAttribute
    public void addAttribute(Principal principal, Model model) {
        if (principal != null) {
            String email = principal.getName();
            User user = userDataService.getUserByEmail(email);
            model.addAttribute("user", user);
        }
    }
}
