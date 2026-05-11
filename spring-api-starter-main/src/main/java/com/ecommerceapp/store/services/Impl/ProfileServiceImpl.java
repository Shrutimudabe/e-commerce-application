package com.ecommerceapp.store.services.Impl;

import com.ecommerceapp.store.dtos.ProfileRequestDto;
import com.ecommerceapp.store.dtos.ProfileResponseDto;
import com.ecommerceapp.store.entities.Profile;
import com.ecommerceapp.store.entities.User;
import com.ecommerceapp.store.mappers.ProfileMapper;
import com.ecommerceapp.store.repositories.ProfileRepository;
import com.ecommerceapp.store.repositories.UserRepository;
import com.ecommerceapp.store.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    @Override
    public ProfileResponseDto createOrUpdateProfile(Long userId, ProfileRequestDto request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Profile profile = profileRepository.findById(userId)
                .orElse(Profile.builder()
                        .user(user)
                        .loyaltyPoints(0)
                        .build());

        ProfileMapper.updateEntity(profile, request);

        Profile saved = profileRepository.save(profile);

        return ProfileMapper.toDto(saved);
    }

    @Override
    public ProfileResponseDto getProfile(Long userId) {

        Profile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        return ProfileMapper.toDto(profile);
    }
}
