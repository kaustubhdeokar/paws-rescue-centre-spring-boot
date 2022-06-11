package deokar.kaustubh.pawsrescuecenter.service;

import deokar.kaustubh.pawsrescuecenter.dto.product.ProductDto;
import deokar.kaustubh.pawsrescuecenter.exceptions.ProductNotExistException;
import deokar.kaustubh.pawsrescuecenter.model.Category;
import deokar.kaustubh.pawsrescuecenter.model.Product;
import deokar.kaustubh.pawsrescuecenter.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public void addProduct(ProductDto productDto, Category category) {
        Product product = creatProductFromDto(productDto, category);
        productRepository.save(product);
    }

    public void updateProduct(Integer productId, ProductDto newProductDto, Category category) {

        Product updatedProduct = creatProductFromDto(newProductDto, category);
        updatedProduct.setId(productId);
        productRepository.save(updatedProduct);
    }

    private Product creatProductFromDto(ProductDto productDto, Category category) {
        return new Product(productDto.getName(), productDto.getImageURL(), productDto.getPrice(), productDto.getDescription(), category);
    }

    public List<ProductDto> listProducts() {
        return productRepository.findAll().stream().map(product -> new ProductDto(product)).collect(Collectors.toList());
    }

    public Optional<Product> readProduct(Integer productId) {
        return productRepository.findById(productId);
    }

    public Product getProductById(Integer productId) throws ProductNotExistException {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (!optionalProduct.isPresent())
            throw new ProductNotExistException("Product id is invalid " + productId);
        return optionalProduct.get();
    }
}
