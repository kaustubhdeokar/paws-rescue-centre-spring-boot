package deokar.kaustubh.pawsrescuecenter.controller;

import deokar.kaustubh.pawsrescuecenter.common.ApiResponse;
import deokar.kaustubh.pawsrescuecenter.dto.product.ProductDto;
import deokar.kaustubh.pawsrescuecenter.exceptions.AuthenticationFailException;
import deokar.kaustubh.pawsrescuecenter.exceptions.CustomException;
import deokar.kaustubh.pawsrescuecenter.model.Product;
import deokar.kaustubh.pawsrescuecenter.model.User;
import deokar.kaustubh.pawsrescuecenter.model.WishList;
import deokar.kaustubh.pawsrescuecenter.repo.ProductRepository;
import deokar.kaustubh.pawsrescuecenter.service.AuthenticationService;
import deokar.kaustubh.pawsrescuecenter.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/wishlist")
public class WishListController {

    @Autowired
    WishListService wishListService;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    private ProductRepository productRepository;


    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addWishList(@RequestBody ProductDto productDto, @RequestParam("token") String token) throws AuthenticationFailException, CustomException {

        authenticationService.authenticate(token);
        User user = authenticationService.getUser(token);
        Optional<Product> product = productRepository.findById(productDto.getId());
        if (product.isPresent()) {
            WishList wishList = new WishList(user, product.get());
            wishListService.createWishList(wishList);
            return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Added to wishlist"), HttpStatus.CREATED);
        }
        else{
            throw new CustomException("product not present");
        }
    }
}
