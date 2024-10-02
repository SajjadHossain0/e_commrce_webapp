/*   Index
1. admin dashboard
2. view user
3. manage advertisement
4. manage categories
5. manage sub-categories

*/

package com.ecommrce.e_commrce_webapp.Controllers;

import com.ecommrce.e_commrce_webapp.Entities.Advertisement;
import com.ecommrce.e_commrce_webapp.Entities.Category;
import com.ecommrce.e_commrce_webapp.Entities.SubCategory;
import com.ecommrce.e_commrce_webapp.Repositories.AdvertisementRepository;
import com.ecommrce.e_commrce_webapp.Repositories.CategoryRepository;
import com.ecommrce.e_commrce_webapp.Repositories.SubCategoryRepository;
import com.ecommrce.e_commrce_webapp.Services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SubCategoryRepository subCategoryRepository;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/admin_dashboard")
    public String admin() {
        return "admin/admin_dashboard";
    }

    @GetMapping("/view_users")
    public String viewUsers() {
        return "admin/view_users";
    }

/* ===============Advertisement===================== */
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


    @GetMapping("/deleteAdvertisement/{id}")
    public String deleteAdvertisement(@PathVariable("id") Long id) {
        advertisementRepository.deleteById(id);  // Delete advertisement by ID
        return "redirect:/admin/add_advertisement";  // Redirect back to the advertisement page
    }
/* ===============Advertisement end===================== */


/*=================Category controllers===================*/
    @GetMapping("/manage_categories")
    public String manage_categories(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "admin/manage_categories";
    }

    @GetMapping
    public List<Category> getAllCategories(Model model) {

        return categoryService.getAllCategories();
    }

    @PostMapping("/add")
    public String addCategory(@RequestParam("name") String name,
                              @RequestParam("coverPhoto") MultipartFile coverPhoto) {
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

    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/admin/manage_categories";
    }

/*=================Category controllers end===================*/

/*=================Sub-Category controllers===================*/

    @GetMapping("/subcategory")
    public String subcategoryPage(Model model) {
        List<SubCategory> subCategories = subCategoryRepository.findAll();
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("subCategories", subCategories);
        model.addAttribute("categories", categories);
        return "admin/manage_categories";
    }

    @PostMapping("/addSubCategory")
    public String addSubCategory(@RequestParam("name") String name,
                                 @RequestParam("coverPhoto") MultipartFile coverPhoto,
                                 @RequestParam("categoryId") Long categoryId) {
        try {
            Category category = categoryRepository.findById(categoryId).orElseThrow();
            String coverPhotoBase64 = Base64.getEncoder().encodeToString(coverPhoto.getBytes());
            SubCategory subCategory = new SubCategory(name, coverPhotoBase64, category);
            subCategoryRepository.save(subCategory);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/admin/manage_categories";
    }

    @PostMapping("/editSubCategory")
    public String editSubCategory(@RequestParam("id") Long id,
                                  @RequestParam("name") String name,
                                  @RequestParam(value = "coverPhoto", required = false) MultipartFile coverPhoto) {
        try {
            Optional<SubCategory> optionalSubCategory = subCategoryRepository.findById(id);
            if (optionalSubCategory.isPresent()) {
                SubCategory subCategory = optionalSubCategory.get();
                subCategory.setName(name);
                if (!coverPhoto.isEmpty()) {
                    String coverPhotoBase64 = Base64.getEncoder().encodeToString(coverPhoto.getBytes());
                    subCategory.setCoverPhoto(coverPhotoBase64);
                }
                subCategoryRepository.save(subCategory);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/admin/manage_categories";
    }

    @GetMapping("/deleteSubCategory/{id}")
    public String deleteSubCategory(@PathVariable Long id) {
        subCategoryRepository.deleteById(id);
        return "redirect:/admin/subcategory";
    }

/*=================Sub-Category controllers end===================*/







}
