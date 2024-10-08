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
    private ProductRepository productRepository;

    public void addToCart(User user, Long productId, int quantity) {
        // Retrieve the product by ID
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Find cart by user, if not found create a new one
        Cart cart = cartRepository.findByUser(user);

        // If the user doesn't have a cart, create a new one
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);  // associate the cart with the user
            cart.setItems(new ArrayList<>());  // initialize the list of items
        }

        // Check if the product is already in the cart
        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            // Product already in the cart, update quantity and total price
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            existingItem.setTotalPrice(existingItem.getQuantity() * product.getPrice());
        } else {
            // Add new item to the cart
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(quantity); // Set the quantity here
            newItem.setTotalPrice(quantity * product.getPrice());
            newItem.setCart(cart);
            cart.getItems().add(newItem);
        }

        // Update the total price of the cart
        double cartTotalPrice = cart.getItems().stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
        cart.setTotalPrice(cartTotalPrice);

        // Save the cart and the cart items
        cartRepository.save(cart);  // This should cascade and save the cart items as well if properly set up.
    }



}
