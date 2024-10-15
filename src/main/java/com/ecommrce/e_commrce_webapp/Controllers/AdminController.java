package com.ecommrce.e_commrce_webapp.Controllers;

import com.ecommrce.e_commrce_webapp.Entities.*;
import com.ecommrce.e_commrce_webapp.Repositories.*;
import com.ecommrce.e_commrce_webapp.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    // Autowiring services and repositories
    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SubCategoryService subCategoryService;

    @Autowired
    private UserDataService userDataService;

    /**
     * Load the admin dashboard page
     */
    @GetMapping("/admin_dashboard")
    public String adminDashboard() {
        return "admin/admin_dashboard";
    }

    /* =============== User Management =============== */

    /**
     * Fetch and display all users in the system.
     */
    @GetMapping("/view_users")
    public String viewUsers(Model model) {
        List<User> users = userDataService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/view_users";
    }

    /**
     * Set the specified user as a seller.
     */
    @GetMapping("/make-seller/{id}")
    public String makeSeller(@PathVariable Long id) {
        updateUserRole(id, "SELLER");
        return "redirect:/admin/view_users";
    }

    /**
     * Revoke seller role from the specified user.
     */
    @GetMapping("/remove-seller/{id}")
    public String removeSeller(@PathVariable Long id) {
        updateUserRole(id, "USER");
        return "redirect:/admin/view_users";
    }

    /**
     * Grant admin role to the specified user.
     */
    @GetMapping("/make-admin/{id}")
    public String makeAdmin(@PathVariable Long id) {
        updateUserRole(id, "ADMIN");
        return "redirect:/admin/view_users";
    }

    /**
     * Block the specified user from accessing the system.
     */
    @GetMapping("/blockUser/{id}")
    public String blockUser(@PathVariable Long id) {
        User user = userDataService.getUserByID(id);
        user.setActive(false);
        userDataService.saveUser(user);
        return "redirect:/admin/view_users";
    }

    /**
     * Unblock the specified user, restoring access.
     */
    @GetMapping("/unblockUser/{id}")
    public String unblockUser(@PathVariable Long id) {
        User user = userDataService.getUserByID(id);
        user.setActive(true);
        userDataService.saveUser(user);
        return "redirect:/admin/view_users";
    }

    /**
     * Permanently delete the specified user.
     */
    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id) {
        userDataService.deleteUser(id);
        return "redirect:/admin/view_users";
    }

    /**
     * Helper method to update the role of a user.
     */
    private void updateUserRole(Long id, String role) {
        User user = userDataService.getUserByID(id);
        user.setRole(role);
        userDataService.saveUser(user);
    }

    /* =============== Advertisement Management =============== */

    /**
     * Load the advertisement management page, displaying all advertisements.
     */
    @GetMapping("/add_advertisement")
    public String addAdvertisement(Model model) {
        List<Advertisement> ads = advertisementRepository.findAll()
                .stream()
                .map(ad -> {
                    ad.setBase64Image(Base64.getEncoder().encodeToString(ad.getImageData()));
                    return ad;
                })
                .collect(Collectors.toList());

        model.addAttribute("advertisements", ads);
        return "admin/add_advertisement";
    }

    /**
     * Upload a new advertisement with an image.
     */
    @PostMapping("/uploadAdvertisement")
    public String uploadAdvertisement(@RequestParam("imageFile") MultipartFile file) throws IOException {
        Advertisement ad = new Advertisement();
        ad.setImageData(file.getBytes());

        String formattedDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm a dd MMM yyyy"));
        ad.setUploadDate(formattedDateTime);

        advertisementRepository.save(ad);
        return "redirect:/admin/add_advertisement";
    }

    /**
     * Delete the specified advertisement.
     */
    @GetMapping("/deleteAdvertisement/{id}")
    public String deleteAdvertisement(@PathVariable("id") Long id) {
        advertisementRepository.deleteById(id);
        return "redirect:/admin/add_advertisement";
    }

    /* =============== Category Management =============== */

    /**
     * Load the category management page, displaying all categories.
     */
    @GetMapping("/manage_categories")
    public String manageCategories(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "admin/manage_categories";
    }

    /**
     * Add a new category with a cover photo.
     */
    @PostMapping("/add")
    public String addCategory(@RequestParam("name") String name,
                              @RequestParam("coverPhoto") MultipartFile coverPhoto) throws IOException {
        String encodedImage = Base64.getEncoder().encodeToString(coverPhoto.getBytes());
        Category category = new Category(name, encodedImage);
        categoryService.saveCategory(category);
        return "redirect:/admin/manage_categories";
    }

    /**
     * Edit an existing category.
     */
    @PostMapping("/edit")
    public String editCategory(@RequestParam("id") Long id,
                               @RequestParam("name") String name,
                               @RequestParam("coverPhoto") MultipartFile coverPhoto) throws IOException {
        Category category = categoryService.getCategoryById(id);
        category.setName(name);
        category.setCoverPhoto(Base64.getEncoder().encodeToString(coverPhoto.getBytes()));
        categoryService.saveCategory(category);
        return "redirect:/admin/manage_categories";
    }

    /**
     * Delete the specified category.
     */
    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/admin/manage_categories";
    }

    /* =============== Sub-Category Management =============== */

    /**
     * Add a new sub-category under the specified category.
     */
    @PostMapping("/addSubCategory")
    public String addSubCategory(@RequestParam("name") String name,
                                 @RequestParam("coverPhoto") MultipartFile coverPhoto,
                                 @RequestParam("categoryId") Long categoryId) throws IOException {
        Category category = categoryRepository.findById(categoryId).orElseThrow();
        String coverPhotoBase64 = Base64.getEncoder().encodeToString(coverPhoto.getBytes());
        SubCategory subCategory = new SubCategory(name, coverPhotoBase64, category);
        subCategoryRepository.save(subCategory);
        return "redirect:/admin/manage_categories";
    }

    /**
     * Edit an existing sub-category.
     */
    @PostMapping("/editSubCategory")
    public String editSubCategory(@RequestParam("id") Long id,
                                  @RequestParam("name") String name,
                                  @RequestParam(value = "coverPhoto", required = false) MultipartFile coverPhoto) throws IOException {
        SubCategory subCategory = subCategoryRepository.findById(id).orElseThrow();
        subCategory.setName(name);
        if (coverPhoto != null && !coverPhoto.isEmpty()) {
            subCategory.setCoverPhoto(Base64.getEncoder().encodeToString(coverPhoto.getBytes()));
        }
        subCategoryRepository.save(subCategory);
        return "redirect:/admin/manage_categories";
    }

    /**
     * Delete the specified sub-category.
     */
    @GetMapping("/deleteSubCategory/{id}")
    public String deleteSubCategory(@PathVariable Long id) {
        subCategoryRepository.deleteById(id);
        return "redirect:/admin/manage_categories";
    }

    /* =============== Shared Methods =============== */

    /**
     * Add common model attributes for the navbar.
     */
    @ModelAttribute
    public void addAttributes(Principal principal, Model model) {
        List<Category> categories = categoryService.getAllCategoriesWithSubCategories();
        model.addAttribute("categoryForNavbar", categories);

        List<SubCategory> subCategories = subCategoryService.getAllSubCategories();
        model.addAttribute("subCategories", subCategories);
    }
}
