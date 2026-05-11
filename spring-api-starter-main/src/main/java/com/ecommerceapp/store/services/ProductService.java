package com.ecommerceapp.store.services;

import com.ecommerceapp.store.dtos.ProductRequestDto;
import com.ecommerceapp.store.dtos.ProductResponseDto;

import com.ecommerceapp.store.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface ProductService {

    ProductResponseDto createProduct(ProductRequestDto request);

    ProductResponseDto getProductById(Long id);

    public Page<ProductResponseDto> getProducts(Pageable pageable);

    ProductResponseDto updateProduct(Long id, ProductRequestDto request);

    void deleteProduct(Long id);



}