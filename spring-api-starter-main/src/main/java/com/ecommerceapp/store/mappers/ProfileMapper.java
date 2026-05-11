package com.ecommerceapp.store.mappers;

import com.ecommerceapp.store.dtos.ProfileRequestDto;
import com.ecommerceapp.store.dtos.ProfileResponseDto;
import com.ecommerceapp.store.entities.Profile;

public class ProfileMapper {

    public static ProfileResponseDto toDto(Profile profile) {
        return ProfileResponseDto.builder()
                .userId(profile.getId())
                .bio(profile.getBio())
                .phoneNumber(profile.getPhoneNumber())
                .dateOfBirth(profile.getDateOfBirth())
                .loyaltyPoints(profile.getLoyaltyPoints())
                .build();
    }

    public static void updateEntity(Profile profile, ProfileRequestDto dto) {
        profile.setBio(dto.getBio());
        profile.setPhoneNumber(dto.getPhoneNumber());
        profile.setDateOfBirth(dto.getDateOfBirth());
    }
}