package com.ecommrce.e_commrce_webapp.Configurations;

import com.ecommrce.e_commrce_webapp.Entities.User;
import com.ecommrce.e_commrce_webapp.Repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Configuration
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    // Autowire the UserRepository to interact with the User entity in the database.
    @Autowired
    private UserRepository userRepository;

    // This method is called whenever authentication is successful.
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        // Retrieve the roles of the authenticated user.
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        // Redirect the user based on their role.
        if (roles.contains("ROLE_ADMIN")) {
            response.sendRedirect("/admin/admin_dashboard");
        } else if (roles.contains("ROLE_SELLER")) {
            response.sendRedirect("/seller/seller_dashboard");
        }  else if (roles.contains("ROLE_USER")) {
            response.sendRedirect("/");
        } else {
            response.sendRedirect("/");
        }


        // Update the last login time for the authenticated user.
        String email = authentication.getName(); // Get the authenticated user's email
        User user = userRepository.findByEmail(email); // Find the user entity by email.
        if (user != null) {

            // Format the current date and time to a specific pattern.
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a dd MMM yyyy");
            String formattedDateTime = LocalDateTime.now().format(formatter);
            user.setLastLoginTime(formattedDateTime); // Update the user's last login time.
            userRepository.save(user); // Save the updated user entity back to the database.
        }
    }
}