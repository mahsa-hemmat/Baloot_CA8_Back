package com.baloot.service;

import com.baloot.exception.CommodityNotFoundException;
import com.baloot.exception.InValidInputException;
import com.baloot.model.Category;
import com.baloot.model.Commodity;
import com.baloot.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository repo;
    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.repo = categoryRepository;
    }
    public void save(Category category) {
        repo.save(category);
    }
    public Category getCategoryByType(String type) {
        Optional<Category> result = repo.findByType(type);
        return result.orElse(null);
    }
}
