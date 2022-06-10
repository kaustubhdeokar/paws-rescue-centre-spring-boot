package deokar.kaustubh.pawsrescuecenter.controller;

import deokar.kaustubh.pawsrescuecenter.common.ApiResponse;
import deokar.kaustubh.pawsrescuecenter.dto.product.ProductDto;
import deokar.kaustubh.pawsrescuecenter.exceptions.AuthenticationFailException;
import deokar.kaustubh.pawsrescuecenter.exceptions.CustomException;
import deokar.kaustubh.pawsrescuecenter.model.Product;
import deokar.kaustubh.pawsrescuecenter.model.User;
import deokar.kaustubh.pawsrescuecenter.model.WishList;
import deokar.kaustubh.pawsrescuecenter.repo.ProductRepository;
import deokar.kaustubh.pawsrescuecenter.repo.UserRepository;
import deokar.kaustubh.pawsrescuecenter.service.AuthenticationService;
import deokar.kaustubh.pawsrescuecenter.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
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

    @GetMapping("/{token}")
    public ResponseEntity<List<ProductDto>> getWishList(@PathVariable("token") String token) throws AuthenticationFailException {

        authenticationService.authenticate(token);
        User user = authenticationService.getUser(token);
        List<WishList> wishLists = wishListService.readWishList(user);
        List<ProductDto> productDtos = new ArrayList<>();
        for(WishList wishList:wishLists){
            productDtos.add(new ProductDto(wishList.getProduct()));
        }
        return new ResponseEntity<>(productDtos,HttpStatus.OK);
    }

}
