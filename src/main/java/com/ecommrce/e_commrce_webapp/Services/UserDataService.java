package com.ecommrce.e_commrce_webapp.Services;

import com.ecommrce.e_commrce_webapp.Entities.User;
import com.ecommrce.e_commrce_webapp.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDataService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByID(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public void saveUser(User user) {
        if (user.getId() == null) {
            // New user, check email uniqueness
            if (isEmailTaken(user.getEmail())) {
                throw new RuntimeException("Email already exists!");
            }
        } else {
            // Update existing user, email check can be skipped if the email has not changed
            User existingUser = userRepository.findById(user.getId()).orElse(null);
            if (existingUser != null && !existingUser.getEmail().equals(user.getEmail()) && isEmailTaken(user.getEmail())) {
                throw new RuntimeException("Email already exists!");
            }
        }
        userRepository.save(user);
    }
    private boolean isEmailTaken(String email) {
        Optional<User> userOpt = Optional.ofNullable(userRepository.findByEmail(email));
        return userOpt.isPresent() && userOpt.get().isActive();
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    // Check if current password is valid
    public boolean checkIfValidOldPassword(User user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    // Change user's password
    public void changeUserPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword)); // Hash the new password
        userRepository.save(user);
    }

}
