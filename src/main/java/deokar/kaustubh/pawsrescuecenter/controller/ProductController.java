package deokar.kaustubh.pawsrescuecenter.controller;

import deokar.kaustubh.pawsrescuecenter.common.ApiResponse;
import deokar.kaustubh.pawsrescuecenter.dto.product.ProductDto;
import deokar.kaustubh.pawsrescuecenter.model.Category;
import deokar.kaustubh.pawsrescuecenter.model.Product;
import deokar.kaustubh.pawsrescuecenter.service.CategoryService;
import deokar.kaustubh.pawsrescuecenter.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody ProductDto productDto) {

        //check if valid category.
        Optional<Category> optionalCategory = categoryService.readCategory(productDto.getCategoryId());

        if (!optionalCategory.isPresent()) {
            return new ResponseEntity<>(new ApiResponse(false, "category does not exist!"), HttpStatus.CONFLICT);
        }

        Category category = optionalCategory.get();
        productService.addProduct(productDto, category);
        return new ResponseEntity<>(new ApiResponse(true, "Product has been added"), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> allProducts = productService.listProducts();
        return new ResponseEntity<>(allProducts, HttpStatus.OK);
    }

    @PostMapping("/update/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable("productId") Integer productId, @RequestBody @Valid ProductDto productDto) {

        Optional<Category> optionalCategory = categoryService.readCategory(productDto.getCategoryId());
        if(!optionalCategory.isPresent()){
            return new ResponseEntity<>(new ApiResponse(false, "category is invalid"), HttpStatus.CONFLICT);
        }
        Category category = optionalCategory.get();

        Optional<Product> optionalProduct = productService.readProduct(productId);
        if(!optionalProduct.isPresent()){
            return new ResponseEntity<>(new ApiResponse(false, "product is invalid"), HttpStatus.CONFLICT);
        }

        productService.updateProduct(productId,productDto,category);
        return new ResponseEntity<>(new ApiResponse(true, "Product has been updated"), HttpStatus.OK);

    }

}