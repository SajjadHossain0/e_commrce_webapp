package com.ecommrce.e_commrce_webapp.Services;

import com.ecommrce.e_commrce_webapp.Entities.*;
import com.ecommrce.e_commrce_webapp.Repositories.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartService = cartService;
    }

    public boolean placeOrder(User user) {
        List<Cart> cartItems = cartService.getCartItemsByUserId(user.getId());

        if (cartItems.isEmpty()) {
            return false;
        }

        // Create a new Order
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus("PENDING");

        double totalAmount = 0;

        // Create OrderItems from CartItems
        List<OrderItem> orderItems = new ArrayList<>();
        for (Cart cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());
            orderItems.add(orderItem);

            totalAmount += orderItem.getPrice();
        }

        // Set total amount and save order
        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItems);

        orderRepository.save(order);
        //cartService.clearCart(user.getId()); // Clear the cart after order is placed

        return true;
    }
}

