package com.ecommrce.e_commrce_webapp.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SellerController {


    @GetMapping("/seller")
    public String seller(){

        return "seller/seller_dashboard";
    }
}

/*
product image 3/4
Product Name
price
short description
more description
Product Details
here is my product display now made me a modal to add product with those info,
and make sure the modal to be proffesional as you customized my other pages


*/