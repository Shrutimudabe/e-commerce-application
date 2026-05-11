package com.ecommerceapp.store.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {

    private Long addressId;
    private String paymentMethod; // COD / ONLINE
}
