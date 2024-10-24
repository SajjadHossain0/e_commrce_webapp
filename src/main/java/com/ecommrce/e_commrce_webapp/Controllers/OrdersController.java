package com.ecommrce.e_commrce_webapp.Controllers;

import com.ecommrce.e_commrce_webapp.Entities.*;
import com.ecommrce.e_commrce_webapp.Repositories.UserRepository;
import com.ecommrce.e_commrce_webapp.Services.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrdersController {

    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;
    private final UserDataService userDataService;
    private final CartService cartService;
    private final UserRepository userRepository;
    private final OrderService orderService;

    public OrdersController(CategoryService categoryService, SubCategoryService subCategoryService, UserDataService userDataService, CartService cartService, UserRepository userRepository, OrderService orderService) {
        this.categoryService = categoryService;
        this.subCategoryService = subCategoryService;
        this.userDataService = userDataService;
        this.cartService = cartService;
        this.userRepository = userRepository;
        this.orderService = orderService;
    }

    @GetMapping("/checkout")
    public String checkout(Model model, Principal principal) {
        if (principal != null) {
            String email = principal.getName();
            User user = userDataService.getUserByEmail(email);

            // Fetch cart items for the user
            List<Cart> cartItems = cartService.getCartItemsByUserId(user.getId());
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("user", user);
        }

        return "checkout";
    }


    @PostMapping("/placeOrder")
    public String placeOrder(@ModelAttribute("order") Order order, Principal principal, HttpSession session) {
        if (principal != null) {
            String email = principal.getName();
            User user = userDataService.getUserByEmail(email);

            // Place the order (save Order and OrderItems)
            boolean isOrderPlaced = orderService.placeOrder(user);

            if (isOrderPlaced) {
                session.setAttribute("successOrder", "Your order has been placed successfully.");
            } else {
                session.setAttribute("errorOrder", "Failed to place the order.");
            }
        }
        return "order_confirmation";
    }

    @GetMapping("/confirmation")
    public String orderConfirmation() {
        return "order_confirmation";
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
