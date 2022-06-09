package deokar.kaustubh.pawsrescuecenter.service;

import deokar.kaustubh.pawsrescuecenter.model.Category;
import deokar.kaustubh.pawsrescuecenter.repo.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public boolean checkIfCategoryExists(Category category) {
        Optional<Category> byId = categoryRepository.findById(category.getId());
        return byId.isPresent();
    }

    public void createCategory(Category category) {
        categoryRepository.save(category);
    }

    public Category readCategory(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName);
    }

    public List<Category> listCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> readCategory(Integer categoryID) {
        return categoryRepository.findById(categoryID);
    }

    public void updateCategory(Integer categoryID, Category newCategory) {
        Category category = categoryRepository.findById(categoryID).get();
        category.setCategoryName(newCategory.getCategoryName());
        category.setDescription(newCategory.getDescription());
        category.setImageUrl(newCategory.getImageUrl());
        categoryRepository.save(category);

    }
}
