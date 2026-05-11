package com.ecommerceapp.store.services;

import com.ecommerceapp.store.dtos.AddressRequestDto;
import com.ecommerceapp.store.entities.Address;

import java.util.List;

public interface AddressService {

    Address addAddress(Long userId, Address address);

    List<Address> getUserAddresses(Long userId);

    Address updateAddress(Long addressId, AddressRequestDto address);

    void deleteAddress(Long addressId);

    Address setDefaultAddress(Long userId, Long addressId);

    Address getDefaultAddress(Long userId);
}
