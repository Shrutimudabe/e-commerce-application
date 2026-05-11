package com.ecommerceapp.store.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressResponseDto {

    private Long id;

    private String street;
    private String city;
    private String state;
    private String zip;

    private boolean isDefault;

    private Long userId;
}
