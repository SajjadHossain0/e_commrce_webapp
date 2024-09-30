package com.ecommrce.e_commrce_webapp.Controllers;

import com.ecommrce.e_commrce_webapp.Entities.Category;
import com.ecommrce.e_commrce_webapp.Services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {


    @Autowired
    private CategoryService categoryService;

    @GetMapping("/")
    public String home(Model model){

        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

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
