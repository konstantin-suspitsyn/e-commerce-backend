package com.github.konstantin.suspitsyn.ecommercebackend.product;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.konstantin.suspitsyn.ecommercebackend.order.ProductsInOrder;
import com.github.konstantin.suspitsyn.ecommercebackend.productcategories.ProductCategory;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sku", unique=true)
    private String sku;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "description")
    private String description;

    @Column(name = "unit_price")
    private Long unitPrice;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "units_in_active_stock")
    private Long unitsInActiveStock;

    @Column(name = "units_in_reserved_stock")
    private Long unitsInReserve;

    @Column(name = "date_created")
    @CreationTimestamp
    private LocalDate dateCreated;

    @Column(name = "last_updated")
    @UpdateTimestamp
    private LocalDate lastUpdated;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "category_id", nullable = false)
    private ProductCategory category;

    @JsonIgnore
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy = "product")
    List<ProductsInOrder> productsInOrderList;

    public Product(String sku, String shortName, String description, Long unitPrice, String imageUrl, Boolean active, Long unitsInActiveStock, Long unitsInReserve, LocalDate dateCreated, LocalDate lastUpdated, ProductCategory category) {
        this.sku = sku;
        this.shortName = shortName;
        this.description = description;
        this.unitPrice = unitPrice;
        this.imageUrl = imageUrl;
        this.active = active;
        this.unitsInActiveStock = unitsInActiveStock;
        this.unitsInReserve = unitsInReserve;
        this.dateCreated = dateCreated;
        this.lastUpdated = lastUpdated;
        this.category = category;
    }
}
