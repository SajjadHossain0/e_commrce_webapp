package com.ecommrce.e_commrce_webapp.Controllers;

import com.ecommrce.e_commrce_webapp.Entities.*;
import com.ecommrce.e_commrce_webapp.Repositories.UserRepository;
import com.ecommrce.e_commrce_webapp.Services.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.security.Principal;
import java.util.*;


@Controller
//@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;
    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;
    private final CartService cartService;
    private UserDataService userDataService;

    public UserController(UserRepository userRepository, UserDataService userDataService, CategoryService categoryService, SubCategoryService subCategoryService, CartService cartService) {
        this.userRepository = userRepository;
        this.userDataService = userDataService;
        this.categoryService = categoryService;
        this.subCategoryService = subCategoryService;
        this.cartService = cartService;
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        String username = userDetails.getUsername();  // Replace with actual user name retrieval logic

        User user = userRepository.findByEmail(username);
        model.addAttribute("user", user);
        model.addAttribute("username", username);

        // Check the roles of the user
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        boolean isSeller = authorities.stream().anyMatch(role -> role.getAuthority().equals("ROLE_SELLER"));
        boolean isAdmin = authorities.stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        // Add flags to the model to show/hide links based on the role
        model.addAttribute("isSeller", isSeller);
        model.addAttribute("isAdmin", isAdmin);

        return "menu/profile";
    }

    @PostMapping("/change-address")
    public String changeAddress(@RequestParam("address") String address, @RequestParam("city") String city,
            @RequestParam("state") String state, @RequestParam("zip") String zip,
            @RequestParam("country") String country, @RequestParam("phone_number") String phone_number, Principal principal) {

        User user = userRepository.findByEmail(principal.getName());
        user.setAddress(address);
        user.setCity(city);
        user.setState(state);
        user.setZip(zip);
        user.setCountry(country);
        user.setPhone_number(phone_number);
        userRepository.save(user);


        return "redirect:/profile";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword, @RequestParam("confirmPassword") String confirmPassword,
                                 Principal principal, RedirectAttributes redirectAttributes) {

        User user = userRepository.findByEmail(principal.getName());

        if (!userDataService.checkIfValidOldPassword(user, currentPassword)) {
            return "redirect:/change-password?error=CurrentPasswordIncorrect";
        }
        // Check if new password and confirm password match
        if (!newPassword.equals(confirmPassword)) {
            return "redirect:/change-password?error=PasswordMismatch";
        }

        // Check if current password matches the actual password
        if (!userDataService.checkIfValidOldPassword(user, currentPassword)) {
            redirectAttributes.addFlashAttribute("error", "Current password is incorrect.");
            return "redirect:/change-password";
        }

        // Check if new password and confirm password match
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "New password and confirm password do not match.");
            return "redirect:/change-password";
        }

        // Save the new password (hashed)
        userDataService.changeUserPassword(user, newPassword);

        return "redirect:/profile";
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        //List<Category> categories = categoryService.getAllCategories();
        List<Category> categories = categoryService.getAllCategoriesWithSubCategories(); // You need to fetch categories with their subcategories
        model.addAttribute("categoryForNavbar", categories);

        List<SubCategory> subCategories = subCategoryService.getAllSubCategories();
        model.addAttribute("subCategories", subCategories);

    }

    @GetMapping("/view-cart")
    public String viewCart(Model model) {

        return "view/view_cart";
    }

    @PostMapping("/cart/add")
    public ResponseEntity<String> addToCart(@AuthenticationPrincipal UserDetails userDetails,@RequestParam Long productId, @RequestParam int quantity, Principal principal) {

        String username = userDetails.getUsername();  // Replace with actual user name retrieval logic
        User user = userRepository.findByEmail(username);

        cartService.addToCart(user, productId, quantity);

        return ResponseEntity.ok("Product added to cart successfully.");
    }



}
