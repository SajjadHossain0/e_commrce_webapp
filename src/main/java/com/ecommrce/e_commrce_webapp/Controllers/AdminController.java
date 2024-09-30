package com.ecommrce.e_commrce_webapp.Controllers;

import com.ecommrce.e_commrce_webapp.Entities.Advertisement;
import com.ecommrce.e_commrce_webapp.Repositories.AdvertisementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
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
        ad.setUploadDate(LocalDateTime.now());  // Store the upload date and time

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
}
