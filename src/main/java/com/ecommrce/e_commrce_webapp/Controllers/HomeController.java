package com.ecommrce.e_commrce_webapp.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(){

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
