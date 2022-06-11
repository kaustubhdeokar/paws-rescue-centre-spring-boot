package deokar.kaustubh.pawsrescuecenter.controller;

import deokar.kaustubh.pawsrescuecenter.common.ApiResponse;
import deokar.kaustubh.pawsrescuecenter.dto.cart.AddToCartDto;
import deokar.kaustubh.pawsrescuecenter.dto.cart.CartDto;
import deokar.kaustubh.pawsrescuecenter.exceptions.AuthenticationFailException;
import deokar.kaustubh.pawsrescuecenter.exceptions.ProductNotExistException;
import deokar.kaustubh.pawsrescuecenter.model.Cart;
import deokar.kaustubh.pawsrescuecenter.model.Product;
import deokar.kaustubh.pawsrescuecenter.model.User;
import deokar.kaustubh.pawsrescuecenter.service.AuthenticationService;
import deokar.kaustubh.pawsrescuecenter.service.CartService;
import deokar.kaustubh.pawsrescuecenter.service.CategoryService;
import deokar.kaustubh.pawsrescuecenter.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;
    @Autowired
    CartService cartService;
    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProductsToCart(@RequestBody AddToCartDto addToCartDto, @RequestParam("token") String token) throws AuthenticationFailException, ProductNotExistException {

        authenticationService.authenticate(token);
        User user = authenticationService.getUser(token);
        Product product = productService.getProductById(addToCartDto.getProductId());
        cartService.addToCart(addToCartDto, product, user);

        return new ResponseEntity<>(new ApiResponse(true, "Added to cart"), HttpStatus.CREATED);

    }

    @GetMapping("/")
    public ResponseEntity<CartDto> getCardItems(@RequestParam("token") String token) throws AuthenticationFailException {
        authenticationService.authenticate(token);
        User user = authenticationService.getUser(token);

        CartDto cartDto = cartService.getCartItemsForUser(user);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

}

