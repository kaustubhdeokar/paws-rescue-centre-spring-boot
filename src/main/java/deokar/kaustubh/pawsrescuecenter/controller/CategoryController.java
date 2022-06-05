package deokar.kaustubh.pawsrescuecenter.controller;

import deokar.kaustubh.pawsrescuecenter.common.ApiResponse;
import deokar.kaustubh.pawsrescuecenter.model.Category;
import deokar.kaustubh.pawsrescuecenter.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createCategory(@Valid @RequestBody Category category) {
        if (Objects.nonNull(categoryService.readCategory(category.getCategoryName()))) {
            return new ResponseEntity<>(new ApiResponse(false, "already exists"), HttpStatus.CONFLICT);
        }
        categoryService.createCategory(category);
        return new ResponseEntity<>(new ApiResponse(true, "category created"), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> allCategories = categoryService.listCategories();
        return new ResponseEntity<>(allCategories, HttpStatus.OK);
    }

    @PostMapping("/update/{categoryID}")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable("categoryID") Integer categoryID, @Valid @RequestBody Category category) {
        if (Objects.nonNull(categoryService.readCategory(categoryID))) {
            categoryService.updateCategory(categoryID, category);
            return new ResponseEntity<>(new ApiResponse(true, "category updated"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(false, "category does not exist"), HttpStatus.NOT_FOUND);

    }

}
