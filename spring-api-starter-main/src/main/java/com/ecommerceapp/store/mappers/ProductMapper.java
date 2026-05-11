package com.ecommerceapp.store.mappers;

import com.ecommerceapp.store.dtos.ProductRequestDto;
import com.ecommerceapp.store.dtos.ProductResponseDto;
import com.ecommerceapp.store.entities.Product;

public class ProductMapper {

    public static Product toEntity(ProductRequestDto dto) {
        return Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .imageUrl(dto.getImage().toString())
                .build(); // category handled separately
    }

    public static ProductResponseDto toDto(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .image(product.getImageUrl())
                .categoryName(
                        product.getCategory() != null
                                ? product.getCategory().getName()
                                : null
                )
                .build();
    }
}
