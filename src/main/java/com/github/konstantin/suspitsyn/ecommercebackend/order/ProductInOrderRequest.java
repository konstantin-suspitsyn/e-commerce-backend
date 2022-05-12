package com.github.konstantin.suspitsyn.ecommercebackend.order;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class ProductInOrderRequest {
    private Long id;
    private Long price;
    private Long quantity;
}
