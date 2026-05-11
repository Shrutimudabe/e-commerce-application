package com.ecommerceapp.store.mappers;

import com.ecommerceapp.store.dtos.AddressRequestDto;
import com.ecommerceapp.store.dtos.AddressResponseDto;
import com.ecommerceapp.store.entities.Address;

public class AddressMapper {

    public static Address toEntity(AddressRequestDto request) {
        return Address.builder()
                .street(request.getStreet())
                .city(request.getCity())
                .state(request.getState())
                .zip(request.getZip())
                .isDefault(request.isDefault())
                .build();
    }

    public static AddressResponseDto toResponse(Address address) {
        return AddressResponseDto.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .zip(address.getZip())
                .isDefault(address.isDefault())
                .userId(
                        address.getUser() != null ? address.getUser().getId() : null
                )
                .build();
    }
}
