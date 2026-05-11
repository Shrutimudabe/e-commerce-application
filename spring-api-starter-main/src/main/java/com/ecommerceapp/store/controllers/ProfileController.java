package com.ecommerceapp.store.controllers;

import com.ecommerceapp.store.dtos.ProfileRequestDto;
import com.ecommerceapp.store.dtos.ProfileResponseDto;
import com.ecommerceapp.store.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    // Create or Update Profile
    @PostMapping("/{userId}")
    public ResponseEntity<ProfileResponseDto> createOrUpdateProfile(
            @PathVariable Long userId,
            @RequestBody ProfileRequestDto request) {

        return ResponseEntity.ok(
                profileService.createOrUpdateProfile(userId, request)
        );
    }

    //Get Profile
    @GetMapping("/{userId}")
    public ResponseEntity<ProfileResponseDto> getProfile(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                profileService.getProfile(userId)
        );
    }
}
