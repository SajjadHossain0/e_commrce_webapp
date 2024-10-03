package com.ecommrce.e_commrce_webapp.Controllers;

import com.ecommrce.e_commrce_webapp.Entities.User;
import com.ecommrce.e_commrce_webapp.Repositories.UserRepository;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/register")
    public String registerForm(Model model) {
        User registeredUser = new User(); // Create a new User object to bind the form data
        model.addAttribute("registeredUser", registeredUser); // Add the User object to the model
        model.addAttribute("success", false); // Attribute to handle success messages
        model.addAttribute("error", false); // Attribute to handle error messages
        return "authentication/register"; // Return the registration form view
    }

    // Handles the registration form submission
    @PostMapping("/register")
    public String registerUser(Model model, @Valid @ModelAttribute("registeredUser") User registeredUser, BindingResult result) {

        // Check if the entered email is already used
        User userForEmailCheck = userRepository.findByEmail(registeredUser.getEmail());
        if (userForEmailCheck != null) {
            // Add an error if the email is already used
            result.addError(new FieldError(
                    "registeredUser",
                    "email",
                    "Email Address is already used. Try another..."));
        }

        // If any validation error occurs, return to the registration page
        if (result.hasErrors()) {
            model.addAttribute("error", true); // Set error attribute to true
            return "authentication/register"; // Return the registration form view
        }

        try {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(); // Password encoder

            // Format the current date and time for registration and last login time
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a dd MMM yyyy");
            String formattedDateTime = LocalDateTime.now().format(formatter);

            // Create a new user object to save in the database
            User newUser = new User();
            newUser.setFirst_name(registeredUser.getFirst_name());
            newUser.setLast_name(registeredUser.getLast_name());
            newUser.setEmail(registeredUser.getEmail());
            newUser.setPassword(bCryptPasswordEncoder.encode(registeredUser.getPassword())); // Encode the password
            newUser.setAddress(registeredUser.getAddress());
            newUser.setCity(registeredUser.getCity());
            newUser.setState(registeredUser.getState());
            newUser.setZip(registeredUser.getZip());
            newUser.setCountry(registeredUser.getCountry());
            newUser.setPhone_number(registeredUser.getPhone_number());

            newUser.setRole("USER"); // Set the user role
            newUser.setLastLoginTime(formattedDateTime); // Set the last login time
            newUser.setRegistrationTime(formattedDateTime); // Set the registration time

            userRepository.save(newUser); // Save the new user in the database

            model.addAttribute("registeredUser", new User()); // Reset the form by binding a new User object
            model.addAttribute("success", true); // Set success attribute to true

        } catch (Exception e) {
            result.rejectValue("first_name", "error.registeredUser", e.getMessage()); // Show the error on the name field
        }

        return "authentication/register"; // Return the registration form view
    }



    // Displays the login form
    @GetMapping("/login")
    public String showLoginPage() {
        return "authentication/login"; // Ensure this matches your template location
    }

}
