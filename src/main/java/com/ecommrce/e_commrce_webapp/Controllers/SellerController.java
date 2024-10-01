package com.ecommrce.e_commrce_webapp.Controllers;

import com.ecommrce.e_commrce_webapp.Entities.Category;
import com.ecommrce.e_commrce_webapp.Services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

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

    @GetMapping("/view_orders")
    public String view_orders(){

        return "seller/view_orders";
    }

    @GetMapping("/view_products")
    public String view_products(){

        return "seller/view_products";
    }





}
