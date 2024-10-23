package com.ecommrce.e_commrce_webapp.Controllers;

import com.ecommrce.e_commrce_webapp.Entities.Cart;
import com.ecommrce.e_commrce_webapp.Entities.Category;
import com.ecommrce.e_commrce_webapp.Entities.SubCategory;
import com.ecommrce.e_commrce_webapp.Entities.User;
import com.ecommrce.e_commrce_webapp.Repositories.UserRepository;
import com.ecommrce.e_commrce_webapp.Services.CartService;
import com.ecommrce.e_commrce_webapp.Services.CategoryService;
import com.ecommrce.e_commrce_webapp.Services.SubCategoryService;
import com.ecommrce.e_commrce_webapp.Services.UserDataService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

@Controller
public class OrdersController {

    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;
    private final UserDataService userDataService;
    private final CartService cartService;
    private final UserRepository userRepository;

    public OrdersController(CategoryService categoryService, SubCategoryService subCategoryService, UserDataService userDataService, CartService cartService, UserRepository userRepository) {
        this.categoryService = categoryService;
        this.subCategoryService = subCategoryService;
        this.userDataService = userDataService;
        this.cartService = cartService;
        this.userRepository = userRepository;
    }

    @GetMapping("/orders/checkout")
    public String checkout() {


        return "place_order";
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
