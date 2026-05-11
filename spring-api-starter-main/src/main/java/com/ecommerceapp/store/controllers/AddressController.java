package com.ecommerceapp.store.controllers;

import com.ecommerceapp.store.dtos.AddressRequestDto;
import com.ecommerceapp.store.dtos.AddressResponseDto;
import com.ecommerceapp.store.entities.Address;
import com.ecommerceapp.store.mappers.AddressMapper;
import com.ecommerceapp.store.services.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/{userId}")
    public ResponseEntity<AddressResponseDto> addAddress(
            @PathVariable Long userId,
            @RequestBody AddressRequestDto request
    ) {
        Address address = addressService.addAddress(userId, AddressMapper.toEntity(request));
        return ResponseEntity.ok(AddressMapper.toResponse(address));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Address>> getAddresses(@PathVariable Long userId) {
        return ResponseEntity.ok(addressService.getUserAddresses(userId));
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressResponseDto> updateAddress(
            @PathVariable Long addressId,
            @RequestBody AddressRequestDto request
    ) {
        Address updated = addressService.updateAddress(addressId, request);
        return ResponseEntity.ok(AddressMapper.toResponse(updated));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId) {
        addressService.deleteAddress(addressId);
        return ResponseEntity.ok("Address deleted successfully");
    }

    @PutMapping("/{userId}/default/{addressId}")
    public ResponseEntity<Address> setDefault(
            @PathVariable Long userId,
            @PathVariable Long addressId
    ) {
        return ResponseEntity.ok(addressService.setDefaultAddress(userId, addressId));
    }

    @GetMapping("/{userId}/default")
    public ResponseEntity<Address> getDefault(@PathVariable Long userId) {
        return ResponseEntity.ok(addressService.getDefaultAddress(userId));
    }
}
