package com.github.konstantin.suspitsyn.ecommercebackend.product;

import com.github.konstantin.suspitsyn.ecommercebackend.productcategories.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p " +
            "from Product p")
    Page<Product> getAll(Pageable pageable);

    Page<Product> findByShortNameContainingIgnoreCase(@RequestParam("shortName") String shortName, Pageable pageable);

    @Query("SELECT p " +
            "from Product p " +
            "WHERE p.category = ?1 ")
    Page<Product> getWhereCategory(ProductCategory productCategory, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Product p " +
            "SET p.unitsInActiveStock = ?1 " +
            ", p.unitsInReserve = ?2 " +
            "WHERE p.id = ?3 ")
    void updateQuantity(Long activeQuantity, Long reservedQuantity, Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Product p " +
            "SET p.sku = ?1 " +
            ", p.shortName = ?2" +
            ", p.description = ?3" +
            ", p.imageUrl = ?4" +
            ", p.lastUpdated = ?5" +
            "WHERE p.id = ?6 ")
    void updateData(String sku, String shortName, String description,
                    String imageUrl, LocalDate lastUpdated, Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Product p " +
            "SET p.unitPrice = ?1 " +
            "WHERE p.id = ?2 ")
    void updatePrice(Long unitPrice, Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Product p " +
            "SET p.active = ?1 " +
            "WHERE p.id = ?2 ")
    void changeActive(boolean active, Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Product p " +
            "SET p.unitsInActiveStock = ?1 " +
            ", p.unitsInReserve = ?2 " +
            "WHERE p.id = ?3 ")
    void updatePcs(Long activeStock, Long reservedStock, Long id);

}
