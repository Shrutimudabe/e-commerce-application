package com.ecommerceapp.store.services.Impl;

import com.ecommerceapp.store.dtos.AddressRequestDto;
import com.ecommerceapp.store.entities.Address;
import com.ecommerceapp.store.entities.User;
import com.ecommerceapp.store.repositories.AddressRepository;
import com.ecommerceapp.store.repositories.UserRepository;
import com.ecommerceapp.store.services.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Override
    public Address addAddress(Long userId, Address address) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        address.setUser(user);

        // If first address → make default
        if (addressRepository.findByUser(user).isEmpty()) {
            address.setDefault(true);
        }

        return addressRepository.save(address);
    }

    @Override
    public List<Address> getUserAddresses(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return addressRepository.findByUser(user);
    }

    @Override
    public Address updateAddress(Long addressId, AddressRequestDto updatedAddress) {

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        address.setStreet(updatedAddress.getStreet());
        address.setCity(updatedAddress.getCity());
        address.setState(updatedAddress.getState());
        address.setZip(updatedAddress.getZip());

        return addressRepository.save(address);
    }

    @Override
    public void deleteAddress(Long addressId) {
        addressRepository.deleteById(addressId);
    }

    @Override
    public Address setDefaultAddress(Long userId, Long addressId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Address> addresses = addressRepository.findByUser(user);

        // Remove old default
        for (Address addr : addresses) {
            addr.setDefault(false);
        }

        Address newDefault = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        newDefault.setDefault(true);

        addressRepository.saveAll(addresses);
        return addressRepository.save(newDefault);
    }

    @Override
    public Address getDefaultAddress(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return addressRepository.findByUserAndIsDefaultTrue(user)
                .orElseThrow(() -> new RuntimeException("Default address not found"));
    }
}
