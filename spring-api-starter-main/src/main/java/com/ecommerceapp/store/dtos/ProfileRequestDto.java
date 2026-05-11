package com.ecommerceapp.store.dtos;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequestDto {

    private String bio;
    private String phoneNumber;
    private LocalDate dateOfBirth;
}