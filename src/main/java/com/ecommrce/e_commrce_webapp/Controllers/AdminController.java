package com.ecommrce.e_commrce_webapp.Controllers;

import com.ecommrce.e_commrce_webapp.Entities.Advertisement;
import com.ecommrce.e_commrce_webapp.Entities.Category;
import com.ecommrce.e_commrce_webapp.Repositories.AdvertisementRepository;
import com.ecommrce.e_commrce_webapp.Services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdvertisementRepository advertisementRepository;

    @GetMapping("/admin_dashboard")
    public String admin() {
        return "admin/admin_dashboard";
    }

    @GetMapping("/add_advertisement")
    public String addAdvertisement(Model model) {
        // Fetch all ads and encode the image data to Base64
        List<Advertisement> ads = advertisementRepository.findAll().stream().map(ad -> {
            String base64Image = Base64.getEncoder().encodeToString(ad.getImageData());
            ad.setBase64Image(base64Image);  // Assuming you've added a 'base64Image' field in the Advertisement entity
            return ad;
        }).collect(Collectors.toList());

        model.addAttribute("advertisements", ads);  // Pass the encoded ads to the view
        return "admin/add_advertisement";
    }

    @PostMapping("/uploadAdvertisement")
    public String uploadAdvertisement(@RequestParam("imageFile") MultipartFile file) throws IOException {
        Advertisement ad = new Advertisement();
        ad.setImageData(file.getBytes());  // Save image data as a byte array in the database

        // Format the current date and time for registration and last login time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a dd MMM yyyy");
        String formattedDateTime = LocalDateTime.now().format(formatter);
        ad.setUploadDate(formattedDateTime);  // Store the upload date and time

        advertisementRepository.save(ad);  // Save the advertisement to the database
        return "redirect:/admin/add_advertisement";  // Redirect to the advertisement page to see the new ad
    }

    @GetMapping("/view_users")
    public String viewUsers() {
        return "admin/view_users";
    }

    @GetMapping("/deleteAdvertisement/{id}")
    public String deleteAdvertisement(@PathVariable("id") Long id) {
        advertisementRepository.deleteById(id);  // Delete advertisement by ID
        return "redirect:/admin/add_advertisement";  // Redirect back to the advertisement page
    }

    /*Category controllers*/

    @GetMapping("/manage_categories")
    public String manage_categories(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "admin/manage_categories";
    }

    @Autowired
    private CategoryService categoryService;

    // Get all categories
    @GetMapping
    public List<Category> getAllCategories(Model model) {

        return categoryService.getAllCategories();
    }

    // Add a new category
    @PostMapping("/add")
    public String addCategory(@RequestParam("name") String name,
                              @RequestParam("coverPhoto") MultipartFile coverPhoto) throws IOException {
        // Convert the cover photo to a Base64 string
        String encodedImage = null;
        try {
            encodedImage = Base64.getEncoder().encodeToString(coverPhoto.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Create and save the new category
        Category category = new Category(name, encodedImage);
        categoryService.saveCategory(category);

        return "redirect:/admin/manage_categories";
    }


    @PostMapping("/edit")
    public String editCategory(@RequestParam("id") Long id,
                               @RequestParam("name") String name,
                               @RequestParam("coverPhoto") MultipartFile coverPhoto) throws IOException {

        Category category = categoryService.getCategoryById(id);

        // Convert the cover photo to a Base64 string
        String encodedImage = Base64.getEncoder().encodeToString(coverPhoto.getBytes());

        // Update category details
        category.setName(name);
        category.setCoverPhoto(encodedImage);

        // Save updated category
        categoryService.saveCategory(category);

        return "redirect:/admin/manage_categories";
    }

    // Optional: Delete category
    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/admin/manage_categories";
    }





}
