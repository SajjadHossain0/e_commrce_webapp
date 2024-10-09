package com.ecommrce.e_commrce_webapp.Repositories;

import com.ecommrce.e_commrce_webapp.Entities.Cart;
import com.ecommrce.e_commrce_webapp.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    public Cart findByUser(User user);
    public List<Cart> findByUserId(Long userId);
    public Cart findByUserIdAndProductId(Long userId, Long productId);
}
