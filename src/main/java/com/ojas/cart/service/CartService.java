package com.ojas.cart.service;

import com.ojas.cart.dto.UpdateCartRequest;
import com.ojas.cart.dto.Cart;
import com.ojas.cart.dto.CartItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CartService {

    Cart getCartByUserEmail(String userEmail) throws Exception;
    List<CartItem> getAllCartItemsByID(Integer cartId) throws Exception;
    Optional<CartItem> findProductInCart(Integer cartId, String productId) throws Exception;
    boolean updateCartItem(UpdateCartRequest request, String userEmail) throws Exception;

    boolean deleteCartByUserEmail(String userEmail) throws Exception;
}
