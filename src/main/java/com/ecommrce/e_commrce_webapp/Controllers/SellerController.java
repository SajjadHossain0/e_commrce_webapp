package com.ecommrce.e_commrce_webapp.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/seller")
public class SellerController {


    @GetMapping("/seller_dashboard")
    public String seller(){

        return "seller/seller_dashboard";
    }

    @GetMapping("/add_product")
    public String add_product(){

        return "seller/add_product";
    }

    @GetMapping("/manage_categories")
    public String manage_categories(){

        return "seller/manage_categories";
    }

    @GetMapping("/view_orders")
    public String view_orders(){

        return "seller/view_orders";
    }

    @GetMapping("/view_products")
    public String view_products(){

        return "seller/view_products";
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