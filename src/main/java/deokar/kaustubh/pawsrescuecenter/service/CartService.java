package deokar.kaustubh.pawsrescuecenter.service;

import deokar.kaustubh.pawsrescuecenter.dto.cart.AddToCartDto;
import deokar.kaustubh.pawsrescuecenter.dto.cart.CartDto;
import deokar.kaustubh.pawsrescuecenter.dto.cart.CartItemDto;
import deokar.kaustubh.pawsrescuecenter.model.Cart;
import deokar.kaustubh.pawsrescuecenter.model.Product;
import deokar.kaustubh.pawsrescuecenter.model.User;
import deokar.kaustubh.pawsrescuecenter.repo.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public void addToCart(AddToCartDto addToCartDto, Product product, User user) {
        Cart cart = new Cart(product, addToCartDto.getQuantity(), user);
        cartRepository.save(cart);
    }

    public CartDto getCartItemsForUser(User user) {

        List<Cart> cartList = cartRepository.findAllByUserOrderByCreatedDateDesc(user);
        List<CartItemDto> cartDtos = new ArrayList<>();
        for (Cart cart : cartList) {
            cartDtos.add(new CartItemDto(cart));
        }
        double totalCost = cartDtos.stream().mapToDouble(dto -> dto.getQuantity() * dto.getProduct().getPrice()).sum();
        return new CartDto(cartDtos, totalCost);
    }
}
