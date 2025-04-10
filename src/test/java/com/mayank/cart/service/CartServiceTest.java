package com.ojas.cart.service;

import com.ojas.cart.dto.Cart;
import com.ojas.cart.dto.CartItem;
import com.ojas.cart.dto.CustomResponse;
import com.ojas.cart.dto.UpdateCartRequest;
import com.ojas.cart.repository.CartItemRepository;
import com.ojas.cart.repository.CartRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CartServiceTest {
    @Autowired
    private CartService cartService;
    @MockBean
    private CartItemRepository cartItemRepository;
    @MockBean
    private CartRepository cartRepository;
    @Test
    public void testGetCartByUserEmailShouldReturnEmpty() throws Exception {
        Optional<Cart> optionalCart = Optional.empty();
        when(cartRepository.findByUserEmail(any())).thenReturn(optionalCart);
        Cart cart =cartService.getCartByUserEmail("ojas@gg.com");
        Assertions.assertEquals("ojas@gg.com", cart.getUserEmail());
    }
    @Test
    public void testGetCartByUserEmail() throws Exception {
        List<CartItem> items = List.of(CartItem.builder().id(2).build());
        Optional<Cart> optionalCart = Optional.of(Cart.builder().userEmail("ojas@gg.com").id(3).items(items).build());
        when(cartRepository.findByUserEmail(any())).thenReturn(optionalCart);
        Cart cart =cartService.getCartByUserEmail("ojas@gg.com");
        Assertions.assertEquals("ojas@gg.com", cart.getUserEmail());
        Assertions.assertEquals(1, cart.getItems().size());
        Assertions.assertEquals(3, cart.getId());
    }
    @Test
    public void testGetAllCartItemsByID() throws Exception {
        List<CartItem> items = List.of(CartItem.builder().id(2).build());
        when(cartItemRepository.findAllByCartId(any())).thenReturn(items);
        List<CartItem> cartItems = cartService.getAllCartItemsByID(3);
        Assertions.assertEquals(1, cartItems.size());
    }
    @Test
    public void testGetAllCartItemsByIDShouldThrow() throws Exception {
        List<CartItem> items = List.of();
        when(cartItemRepository.findAllByCartId(any())).thenReturn(items);
        Assertions.assertThrows(Exception.class,()->cartService.getAllCartItemsByID(3));
    }
    @Test
    public void testDeleteCartByUserEmailEmpty() throws Exception {
        Optional<Cart> optionalCart = Optional.empty();
        when(cartRepository.findByUserEmail(any())).thenReturn(optionalCart);
        boolean status = cartService.deleteCartByUserEmail("ojas@gg.com");
        Assertions.assertEquals(false, status);
    }
    @Test
    public void testDeleteCartByUserEmail() throws Exception {
        List<CartItem> items = List.of(CartItem.builder().id(2).build());
        Optional<Cart> optionalCart = Optional.of(Cart.builder().userEmail("ojas@gg.com").id(3).items(items).build());
        when(cartRepository.findByUserEmail(any())).thenReturn(optionalCart);
        boolean status = cartService.deleteCartByUserEmail("ojas@gg.com");
        verify(cartRepository, times(1)).deleteByUserEmail("ojas@gg.com");
    }
    @Test
    public void testFindProductInCartEmpty() throws Exception {
        Optional<CartItem> optionalCart = Optional.empty();
        when(cartItemRepository.findByProductIdAndCartId(any(), any())).thenReturn(optionalCart);
        Optional<CartItem> item = cartService.findProductInCart(3, "demo");
        Assertions.assertEquals(true, item.isEmpty());
    }
    @Test
    public void testFindProductInCart() throws Exception {
        Cart cart = Cart.builder().id(3).userEmail("ojas@gg.com").build();
        Optional<CartItem> opt = Optional.of(CartItem.builder().productId("demo").quantity(3).id(4).cart(cart).build());
        when(cartItemRepository.findByProductIdAndCartId(any(), any())).thenReturn(opt);
        Optional<CartItem> item = cartService.findProductInCart(3, "demo");
        Assertions.assertEquals(4, item.get().getId());
        Assertions.assertEquals("demo", item.get().getProductId());
        Assertions.assertEquals(3, item.get().getQuantity());
    }
    @Test
    public void testUpdateCartItem() throws Exception {
        Cart cart = Cart.builder().id(3).userEmail("ojas@gg.com").build();
        UpdateCartRequest request = UpdateCartRequest.builder().quantity(2).productId("heya").build();
        Optional<CartItem> opt = Optional.of(CartItem.builder().productId("demo").quantity(3).id(4).cart(cart).build());
        when(cartItemRepository.findByProductIdAndCartId(any(), any())).thenReturn(opt);
        boolean status = cartService.updateCartItem(request, "ojas@gg.com");
        Assertions.assertTrue(status);
    }
    @Test
    public void testUpdateCartItemEmpty() throws Exception {
        Cart cart = Cart.builder().id(3).userEmail("ojas@gg.com").build();
        UpdateCartRequest request = UpdateCartRequest.builder().quantity(2).productId("heya").build();
        Optional<CartItem> opt = Optional.empty();
        when(cartItemRepository.findByProductIdAndCartId(any(), any())).thenReturn(opt);
        boolean status = cartService.updateCartItem(request, "ojas@gg.com");
        Assertions.assertTrue(status);
    }
    @Test
    public void testUpdateCartItemDelete() throws Exception {
        Cart cart = Cart.builder().id(3).userEmail("ojas@gg.com").build();
        UpdateCartRequest request = UpdateCartRequest.builder().quantity(2).productId("heya").build();
        Optional<CartItem> opt = Optional.of(CartItem.builder().productId("demo").quantity(0).id(4).cart(cart).build());
        when(cartItemRepository.findByProductIdAndCartId(any(), any())).thenReturn(opt);
        when(cartItemRepository.deleteByProductIdAndCartId(any(), any())).thenReturn(1);
        boolean status = cartService.updateCartItem(request, "ojas@gg.com");
        Assertions.assertTrue(status);
    }
    @Test
    public void testUpdateCartItemQuantityAdd() throws Exception {
        Cart cart = Cart.builder().id(3).userEmail("ojas@gg.com").build();
        UpdateCartRequest request = UpdateCartRequest.builder().quantity(2).quantityAdd(4).productId("heya").build();
        Optional<CartItem> opt = Optional.of(CartItem.builder().productId("demo").quantity(6).id(4).cart(cart).build());
        when(cartItemRepository.findByProductIdAndCartId(any(), any())).thenReturn(opt);
        when(cartItemRepository.deleteByProductIdAndCartId(any(), any())).thenReturn(1);
        boolean status = cartService.updateCartItem(request, "ojas@gg.com");
        Assertions.assertTrue(status);
    }
}
