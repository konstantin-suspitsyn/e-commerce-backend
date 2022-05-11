package com.github.konstantin.suspitsyn.ecommercebackend.product;

import com.github.konstantin.suspitsyn.ecommercebackend.productcategories.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import java.util.Date;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class ProductCreateRequest {
    private String sku;
    private String shortName;
    private String description;
    private Long unitPrice;
    private String imageUrl;
    private Boolean active;
    private Long unitsInActiveStock;
    private Long unitsInReserve;
    private Long category;
}
