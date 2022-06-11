package deokar.kaustubh.pawsrescuecenter.service;

import deokar.kaustubh.pawsrescuecenter.dto.cart.AddToCartDto;
import deokar.kaustubh.pawsrescuecenter.dto.cart.CartDto;
import deokar.kaustubh.pawsrescuecenter.dto.cart.CartItemDto;
import deokar.kaustubh.pawsrescuecenter.exceptions.CartItemNotExistException;
import deokar.kaustubh.pawsrescuecenter.model.Cart;
import deokar.kaustubh.pawsrescuecenter.model.Product;
import deokar.kaustubh.pawsrescuecenter.model.User;
import deokar.kaustubh.pawsrescuecenter.repo.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public void deleteCartItem(Integer cartItemId, User user) throws CartItemNotExistException {
        Optional<Cart> optionalCart = cartRepository.findById(cartItemId);
        if(!optionalCart.isPresent()){
            throw new CartItemNotExistException("does not exist");
        }
        if (optionalCart.get().getUser() != user) {
            throw new CartItemNotExistException("cart item does not belong to user");
        }
        cartRepository.deleteById(optionalCart.get().getId());

//        cartRepository.save()
    }
}
