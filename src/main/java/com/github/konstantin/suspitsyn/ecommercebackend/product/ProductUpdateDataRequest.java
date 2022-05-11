package com.github.konstantin.suspitsyn.ecommercebackend.product;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class ProductUpdateDataRequest {
    private Long id;
    private String sku;
    private String shortName;
    private String description;
    private String imageUrl;
}
