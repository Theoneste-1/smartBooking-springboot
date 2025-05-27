package com.bookingsmart.services.booking;

import com.bookingsmart.models.Category;
import com.bookingsmart.repositories.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public Category createCategory(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new RuntimeException("Category with this name already exists");
        }
        return categoryRepository.save(category);
    }

    public Page<Category> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    public List<Category> getAllActiveCategories() {
        return categoryRepository.findByIsActive(true);
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Optional<Category> getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Transactional
    public Category updateCategory(Long id, Category categoryDetails) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Check if the new name is already taken by another category
        if (!category.getName().equals(categoryDetails.getName()) &&
                categoryRepository.existsByName(categoryDetails.getName())) {
            throw new RuntimeException("Category with this name already exists");
        }

        category.setName(categoryDetails.getName());
        category.setDescription(categoryDetails.getDescription());
        category.setActive(categoryDetails.isActive());

        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Soft delete - just mark as inactive
        category.setActive(false);
        categoryRepository.save(category);
    }

    @Transactional
    public void activateCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setActive(true);
        categoryRepository.save(category);
    }

    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }

    public long countActiveCategories() {
        return categoryRepository.countByIsActive(true);
    }

    public List<Category> getCategoriesWithServices() {
        return categoryRepository.findByIsActiveAndServicesIsNotEmpty(true);
    }
}