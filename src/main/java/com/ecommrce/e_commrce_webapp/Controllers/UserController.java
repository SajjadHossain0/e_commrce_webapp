package com.ecommrce.e_commrce_webapp.Controllers;

import com.ecommrce.e_commrce_webapp.Entities.User;
import com.ecommrce.e_commrce_webapp.Repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;
import java.util.List;

@Controller
//@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
