package com.ecommrce.e_commrce_webapp.Services;

import com.ecommrce.e_commrce_webapp.Entities.*;
import com.ecommrce.e_commrce_webapp.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserDataService userDataService;
    @Autowired
    private ProductService productService;

    public Cart addToCart(Long productId, Long userId){
        User user = userDataService.getUserByID(userId);
        Product product = productService.getProductById(productId);

        Cart existingCart = cartRepository.findByUserIdAndProductId(userId, productId);

        if (existingCart != null) {
            // If the product is already in the cart, return null or handle accordingly
            return null;
        }

        Cart cartStatus = cartRepository.findByUserIdAndProductId(userId, productId);
        Cart cart = null;
        if (cartStatus == null) {
            cart = new Cart();
            cart.setUser(user);
            cart.setProduct(product);
            cart.setQuantity(1);
            cart.setTotalprice(1* product.getPrice());
        }
        else {
            cart.setQuantity(cart.getQuantity() + 1);
            cart.setTotalprice(cart.getQuantity() * cart.getProduct().getPrice());
        }
        Cart saveCart = cartRepository.save(cart);


        return saveCart;
    }

    public Cart getCartByUser(Long userId){

        return null;
    }



}
