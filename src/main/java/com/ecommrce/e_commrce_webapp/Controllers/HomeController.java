package com.ecommrce.e_commrce_webapp.Controllers;

import com.ecommrce.e_commrce_webapp.Entities.Advertisement;
import com.ecommrce.e_commrce_webapp.Entities.Category;
import com.ecommrce.e_commrce_webapp.Repositories.AdvertisementRepository;
import com.ecommrce.e_commrce_webapp.Services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {


    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AdvertisementRepository advertisementRepository;

    @GetMapping("/")
    public String home(Model model){

        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        // Fetch all ads and encode the image data to Base64
        List<Advertisement> ads = advertisementRepository.findAll().stream().map(ad -> {
            String base64Image = Base64.getEncoder().encodeToString(ad.getImageData());
            ad.setBase64Image(base64Image);  // Assuming you've added a 'base64Image' field in the Advertisement entity
            return ad;
        }).collect(Collectors.toList());
        model.addAttribute("advertisements", ads);


        return "home";
    }


    @GetMapping("/category")
    public String category(){

        return "view/view_category";
    }

    @GetMapping("/product")
    public String product(){

        return "view/view_product";
    }


}
