package com.ecommerceapp.store.services.Impl;

import com.ecommerceapp.store.dtos.ProductRequestDto;
import com.ecommerceapp.store.dtos.ProductResponseDto;
import com.ecommerceapp.store.entities.Category;
import com.ecommerceapp.store.entities.Product;
import com.ecommerceapp.store.mappers.ProductMapper;
import com.ecommerceapp.store.repositories.CategoryRepository;
import com.ecommerceapp.store.repositories.ProductRepository;
import com.ecommerceapp.store.services.FileService;
import com.ecommerceapp.store.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final FileService fileService;

    @Override
    public ProductResponseDto createProduct(ProductRequestDto request) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        String imagePath = null;

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            imagePath = fileService.uploadFile(request.getImage());

        }

        System.out.println("Image: " + request.getImage());
        System.out.println("Image path: " + imagePath);

        Product product = ProductMapper.toEntity(request);
        product.setImageUrl(imagePath);
        product.setCategory(category);

        Product saved = productRepository.save(product);

        return ProductMapper.toDto(saved);
    }

    @Override
    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return ProductMapper.toDto(product);
    }

    @Override
    public Page<ProductResponseDto> getProducts(Pageable pageable) {

        return productRepository.findAll(pageable)
                .map(ProductMapper::toDto);
    }

    @Override
    public ProductResponseDto updateProduct(Long id, ProductRequestDto request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(category);

        return ProductMapper.toDto(productRepository.save(product));
    }

    @Override
    public void deleteProduct(Long id) {

        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }

        productRepository.deleteById(id);
    }


}