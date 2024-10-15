package com.ecommrce.e_commrce_webapp.Controllers;

import com.ecommrce.e_commrce_webapp.Entities.*;
import com.ecommrce.e_commrce_webapp.Repositories.*;
import com.ecommrce.e_commrce_webapp.Services.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.security.Principal;
import java.util.*;

/**
 * Controller to handle user-related actions such as profile management, cart operations,
 * and updating user information like address and password.
 */
@Controller
public class UserController {

    private final UserRepository userRepository;
    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;
    private final CartService cartService;
    private final UserDataService userDataService;

    public UserController(UserRepository userRepository, UserDataService userDataService,
                          CategoryService categoryService, SubCategoryService subCategoryService,
                          CartService cartService) {
        this.userRepository = userRepository;
        this.userDataService = userDataService;
        this.categoryService = categoryService;
        this.subCategoryService = subCategoryService;
        this.cartService = cartService;
    }

    /**
     * Displays the user profile page.
     * Adds the user and their role information to the model to dynamically show/hide UI elements based on the role.
     *
     * @param userDetails the authenticated user's details
     * @param model the model to pass data to the view
     * @return the profile page
     */
    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String username = userDetails.getUsername();  // Retrieve the logged-in username
        User user = userRepository.findByEmail(username);
        model.addAttribute("user", user);
        model.addAttribute("username", username);

        // Determine if the user has seller or admin roles
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        boolean isSeller = authorities.stream().anyMatch(role -> role.getAuthority().equals("ROLE_SELLER"));
        boolean isAdmin = authorities.stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        // Add role flags to the model for conditional rendering in the view
        model.addAttribute("isSeller", isSeller);
        model.addAttribute("isAdmin", isAdmin);

        return "menu/profile";
    }

    /**
     * Handles address change requests for a user and updates the user information in the database.
     *
     * @param address the updated address
     * @param city the updated city
     * @param state the updated state
     * @param zip the updated postal code
     * @param country the updated country
     * @param phone_number the updated phone number
     * @param principal the authenticated user's information
     * @return redirects to the profile page after updating the address
     */
    @PostMapping("/change-address")
    public String changeAddress(@RequestParam("address") String address, @RequestParam("city") String city,
                                @RequestParam("state") String state, @RequestParam("zip") String zip,
                                @RequestParam("country") String country, @RequestParam("phone_number") String phone_number,
                                Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        user.setAddress(address);
        user.setCity(city);
        user.setState(state);
        user.setZip(zip);
        user.setCountry(country);
        user.setPhone_number(phone_number);
        userRepository.save(user);  // Persist the updated address

        return "redirect:/profile";
    }

    /**
     * Handles password change requests for a user.
     * Validates the current password and confirms the new password before updating.
     *
     * @param currentPassword the current password of the user
     * @param newPassword the new password to be set
     * @param confirmPassword the confirmation of the new password
     * @param principal the authenticated user's information
     * @param redirectAttributes used to send feedback to the view
     * @return redirects to the profile page after changing the password
     */
    @PostMapping("/change-password")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Principal principal, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByEmail(principal.getName());

        // Validate the current password
        if (!userDataService.checkIfValidOldPassword(user, currentPassword)) {
            redirectAttributes.addFlashAttribute("error", "Current password is incorrect.");
            return "redirect:/change-password";
        }

        // Check if new password and confirm password match
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "New password and confirm password do not match.");
            return "redirect:/change-password";
        }

        // Save the new password
        userDataService.changeUserPassword(user, newPassword);

        return "redirect:/profile";
    }

    /**
     * Adds common data to the model, such as categories and subcategories, for use in the view.
     *
     * @param model the model to pass data to the view
     */
    @ModelAttribute
    public void addAttributes(Model model) {
        List<Category> categories = categoryService.getAllCategoriesWithSubCategories(); // Fetch categories with their subcategories
        model.addAttribute("categoryForNavbar", categories);

        List<SubCategory> subCategories = subCategoryService.getAllSubCategories();
        model.addAttribute("subCategories", subCategories);
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
}

