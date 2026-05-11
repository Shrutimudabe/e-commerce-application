package com.ecommerceapp.store.dtos;
import org.springframework.web.multipart.MultipartFile;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {
    private String name;
    private String description;
    private BigDecimal price;
    private Long categoryId;
    private MultipartFile image;
}