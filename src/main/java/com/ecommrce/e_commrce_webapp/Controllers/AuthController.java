package com.ecommrce.e_commrce_webapp.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {
    @GetMapping("/login")
    public String loginPage(){

        return "authentication/login";
    }

    @GetMapping("/register")
    public String registerPage(){

        return "authentication/register";
    }
}
