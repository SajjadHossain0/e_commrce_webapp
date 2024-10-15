package com.ecommrce.e_commrce_webapp.Controllers;

import com.ecommrce.e_commrce_webapp.Entities.User;
import com.ecommrce.e_commrce_webapp.Repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This controller handles authentication-related actions,
 * such as user registration and login.
 */
@Controller
public class AuthController {

    // Inject UserRepository to interact with the database
    @Autowired
    private UserRepository userRepository;

    /**
     * Displays the registration form to the user.
     *
     * @param model Model object to pass data to the view.
     * @return The registration form view.
     */
    @GetMapping("/register")
    public String registerForm(Model model) {
        // Add an empty User object to the model for form binding
        model.addAttribute("registeredUser", new User());
        // Flags for managing success and error messages
        model.addAttribute("success", false);
        model.addAttribute("error", false);

        // Return the view template for the registration form
        return "authentication/register";
    }

    /**
     * Handles user registration form submission.
     *
     * @param model           Model object to pass data to the view.
     * @param registeredUser  The User object populated from form data.
     * @param result          BindingResult object for validation errors.
     * @return Redirect to the login page if successful, otherwise reload the form.
     */
    @PostMapping("/register")
    public String registerUser(Model model,
                               @Valid @ModelAttribute("registeredUser") User registeredUser,
                               BindingResult result) {

        // Check if the email address is already registered
        User existingUser = userRepository.findByEmail(registeredUser.getEmail());
        if (existingUser != null) {
            // Add error message if email is already in use
            result.addError(new FieldError(
                    "registeredUser", "email", "Email Address is already used. Try another..."
            ));
        }

        // Return to the registration page if there are validation errors
        if (result.hasErrors()) {
            model.addAttribute("error", true); // Set error flag for view
            return "authentication/register"; // Reload the registration form
        }

        try {
            // Create a password encoder instance to encrypt user passwords
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            // Format the current date and time for user registration and last login
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a dd MMM yyyy");
            String formattedDateTime = LocalDateTime.now().format(formatter);

            // Create a new User object and populate it with form data
            User newUser = new User();
            newUser.setFirst_name(registeredUser.getFirst_name());
            newUser.setLast_name(registeredUser.getLast_name());
            newUser.setEmail(registeredUser.getEmail());
            newUser.setPassword(passwordEncoder.encode(registeredUser.getPassword())); // Encrypt the password
            newUser.setAddress(registeredUser.getAddress());
            newUser.setCity(registeredUser.getCity());
            newUser.setState(registeredUser.getState());
            newUser.setZip(registeredUser.getZip());
            newUser.setCountry(registeredUser.getCountry());
            newUser.setPhone_number(registeredUser.getPhone_number());

            newUser.setRole("USER"); // Assign default role as 'USER'
            newUser.setRegistrationTime(formattedDateTime); // Set registration time
            newUser.setLastLoginTime(formattedDateTime); // Set last login time initially to registration time

            // Save the new user in the database
            userRepository.save(newUser);

            // Reset the form for the view after successful registration
            model.addAttribute("registeredUser", new User());
            model.addAttribute("success", true); // Set success flag for the view

        } catch (Exception e) {
            // If an exception occurs, add the error message to the form
            result.rejectValue("first_name", "error.registeredUser", e.getMessage());
        }

        // Redirect to login page after successful registration
        return "authentication/login";
    }

    /**
     * Displays the login form to the user.
     *
     * @return The login form view.
     */
    @GetMapping("/login")
    public String showLoginPage() {
        return "authentication/login"; // Return the view for login form
    }
}
