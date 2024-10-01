package com.ecommrce.e_commrce_webapp.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class MenuController {

    @GetMapping("/profile")
    public String profile(){

        return "menu/profile";
    }
}
