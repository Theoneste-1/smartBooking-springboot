package com.example.smartbooking.services.booking;

import com.example.smartbooking.dto.request.CategoryCreateDto;
import com.example.smartbooking.dto.response.CategoryResponseDto;
import com.example.smartbooking.models.Category;
import com.example.smartbooking.models.Professional;
import com.example.smartbooking.repositories.CategoryRepository;
import com.example.smartbooking.services.user.ProfessionalService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProfessionalService professionalService;

    public CategoryService(CategoryRepository categoryRepository, ProfessionalService professionalService) {
        this.categoryRepository = categoryRepository;
        this.professionalService = professionalService;
    }

    @Transactional
    public CategoryResponseDto createCategory(CategoryCreateDto category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new RuntimeException("Category with this name already exists");
        }
        Category cat = new Category();
        cat.setName(category.getName());
        cat.setActive(true);
        cat.setDescription(category.getDescription());

        Professional prof = professionalService.getSingleProfessional(category.getProfessionalId());
        cat.setProfessional(prof);
        return mapToDto(categoryRepository.save(cat));
    }

    public Page<CategoryResponseDto> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(this::mapToDto);
    }

    public List<CategoryResponseDto> getAllActiveCategories() {
        return categoryRepository.findByIsActive(true).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public Optional<CategoryResponseDto> getCategoryById(Long id) {
        return categoryRepository.findById(id).map(this::mapToDto);
    }

    public Optional<CategoryResponseDto> getCategoryByName(String name) {
        return categoryRepository.findByName(name).map(this::mapToDto);
    }

    @Transactional
    public CategoryResponseDto updateCategory(Long id, Category categoryDetails) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (!category.getName().equals(categoryDetails.getName()) &&
                categoryRepository.existsByName(categoryDetails.getName())) {
            throw new RuntimeException("Category with this name already exists");
        }
        category.setName(categoryDetails.getName());
        category.setDescription(categoryDetails.getDescription());
        category.setActive(categoryDetails.isActive());

        return mapToDto(categoryRepository.save(category));
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
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

    public List<CategoryResponseDto> getCategoriesWithServices() {
        return categoryRepository.findByIsActiveAndServicesIsNotEmpty(true).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


    public Category getSingleCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }
    private CategoryResponseDto mapToDto(Category category) {
        return CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .isActive(category.isActive())
                .professionalId(category.getProfessional().getId())
                .professionalName(category.getProfessional().getBusinessName())
                .build();
    }
}
