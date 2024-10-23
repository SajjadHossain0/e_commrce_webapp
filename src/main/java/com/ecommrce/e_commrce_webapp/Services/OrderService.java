package com.ecommrce.e_commrce_webapp.Services;

import com.ecommrce.e_commrce_webapp.Entities.*;
import com.ecommrce.e_commrce_webapp.Repositories.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public Order createOrder(User user, List<Cart> cartItems) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(new Date());

        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0;

        for (Cart cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());
            orderItem.setOrder(order);

            orderItems.add(orderItem);
            totalAmount += orderItem.getPrice();
        }

        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItems);

        return orderRepository.save(order); // Save both order and items due to CascadeType.ALL
    }



}
