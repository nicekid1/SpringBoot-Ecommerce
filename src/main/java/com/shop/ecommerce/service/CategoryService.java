package com.shop.ecommerce.service;

import com.shop.ecommerce.dto.CategoryRequest;
import com.shop.ecommerce.dto.CategoryResponse;
import com.shop.ecommerce.model.Category;
import com.shop.ecommerce.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponse createCategory(CategoryRequest request){
        Category category = Category.builder().
                name(request.getName())
                .description(request.getDescription())
                .build();
        return mapToResponse(categoryRepository.save(category));
    }

    public List<CategoryResponse> getAllCategories(){
        return categoryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public CategoryResponse getAllCategories(Long id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(()->new RuntimeException("category is not found"));
        return mapToResponse(category);
    }

    public void deleteCategory(Long id){
        categoryRepository.deleteById(id);
    }

    private CategoryResponse mapToResponse(Category category){
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }
}
