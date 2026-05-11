package com.ecommerceapp.store.services;

import com.ecommerceapp.store.dtos.ProfileRequestDto;
import com.ecommerceapp.store.dtos.ProfileResponseDto;

public interface ProfileService {

    ProfileResponseDto createOrUpdateProfile(Long userId, ProfileRequestDto request);

    ProfileResponseDto getProfile(Long userId);
}
