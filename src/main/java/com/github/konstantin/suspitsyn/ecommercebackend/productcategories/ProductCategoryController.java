package com.github.konstantin.suspitsyn.ecommercebackend.productcategories;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestMapping(path = "product-categories")
@RestController
@AllArgsConstructor
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    @GetMapping("/all")
    public List<ProductCategory> getAllProductCategories() {
        return productCategoryService.getAllProductCategories();
    }

    @GetMapping("/get")
    public ProductCategory findById(@RequestParam Long id) {
        return productCategoryService.getById(id);
    }

    @PostMapping("/create")
    public ProductCategory createProductCategory(@RequestParam String name, HttpServletRequest request, HttpServletResponse response) {
        return productCategoryService.createProductCategory(name, request, response);
    }

    @PostMapping("/update")
    public void updateProductCategory(@RequestParam String name, @RequestParam Long id, HttpServletRequest request, HttpServletResponse response) {
        productCategoryService.updateProductCategory(name, id, request, response);
    }

    @DeleteMapping("/delete")
    public void deleteProductCategory(@RequestParam Long id) {
        productCategoryService.deleteProductCategory(id);
    }

}
