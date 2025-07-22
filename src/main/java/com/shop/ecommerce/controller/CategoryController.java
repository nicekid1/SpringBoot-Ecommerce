package com.shop.ecommerce.controller;

import com.shop.ecommerce.dto.CategoryRequest;
import com.shop.ecommerce.dto.CategoryResponse;
import com.shop.ecommerce.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryResponse>createCategory(@RequestBody CategoryRequest request){
        return ResponseEntity.ok(categoryService.createCategory(request));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>>getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse>getCategoryById(@PathVariable Long id){
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id){
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("You can't delete this category");
        }
    }
}
