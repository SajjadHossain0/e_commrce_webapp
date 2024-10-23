package com.ecommrce.e_commrce_webapp.Controllers;

import com.ecommrce.e_commrce_webapp.Entities.*;
import com.ecommrce.e_commrce_webapp.Services.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.*;

@Controller
public class CartController {
    private final CartService cartService;
    private final UserDataService userDataService;
    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;
    private final OrderService orderService;

    public CartController(CartService cartService, UserDataService userDataService, CategoryService categoryService, SubCategoryService subCategoryService, OrderService orderService) {
        this.cartService = cartService;
        this.userDataService = userDataService;
        this.categoryService = categoryService;
        this.subCategoryService = subCategoryService;
        this.orderService = orderService;
    }


    /**
     * Displays the cart page for the logged-in user, showing all items in the cart.
     *
     * @param model the model to pass data to the view
     * @param principal the authenticated user's information
     * @return the cart view page
     */
    @GetMapping("/viewCart")
    public String viewCart(Model model, Principal principal) {
        if (principal != null) {
            String email = principal.getName();
            User user = userDataService.getUserByEmail(email);

            // Fetch the cart items for the user
            List<Cart> cartItems = cartService.getCartItemsByUserId(user.getId());
            model.addAttribute("cartItems", cartItems);
        }

        return "view/view_cart";
    }

    /**
     * Adds a product to the cart for the logged-in user.
     *
     * @param productId the ID of the product to add
     * @param userId the ID of the user adding the product
     * @param session the HTTP session for storing messages
     * @return redirects to the cart view after adding the product
     */
    @PostMapping("/addToCart")
    public String addToCart(@RequestParam("pid") Long productId,
                            @RequestParam("uid") Long userId,
                            @RequestParam("quantity") int quantity,
                            HttpSession session) {
        Cart addedToCart = cartService.addToCart(productId, userId, quantity);
        if (addedToCart != null) {
            session.setAttribute("successCart", "Product added to your cart.");
        } else {
            session.setAttribute("errorCart", "Failed to add product to your cart.");
        }

        return "redirect:/viewCart";
    }

    /**
     * Deletes a cart item based on the provided ID.
     *
     * @param id the ID of the cart item to delete
     * @return redirects to the cart view after deleting the cart item
     */
    @GetMapping("/deletecart/{id}")
    public String deleteCart(@PathVariable("id") Long id) {
        cartService.removeFromCart(id);
        return "redirect:/viewCart";
    }


    @PostMapping("/orders/checkout")
    public String checkout(Principal principal, HttpSession session) {
        if (principal != null) {
            String email = principal.getName();
            User user = userDataService.getUserByEmail(email);

            List<Cart> cartItems = cartService.getCartItemsByUserId(user.getId());

            if (cartItems.isEmpty()) {
                session.setAttribute("errorCheckout", "Your cart is empty.");
                return "redirect:/viewCart";
            }

            // Create order and clear the cart
            orderService.createOrder(user, cartItems);
            //cartService.clearCart(user.getId());

            session.setAttribute("successCheckout", "Order placed successfully.");
            return "redirect:/orders/success"; // Redirect to success page
        }

        return "redirect:/login";
    }

    @GetMapping("/orders/success")
    public String success() {

        return "order_success";
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
