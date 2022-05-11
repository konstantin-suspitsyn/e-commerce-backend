package com.github.konstantin.suspitsyn.ecommercebackend.product;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RequestMapping(path = "products")
@RestController
@AllArgsConstructor
public class ProductController {

    public static final int ITEMS_PER_PAGE_STANDARD = 20;
    public static final int FIRST_PAGE_NO = 0;
    ProductService productService;

    @GetMapping
    public Page<Product> getAllProducts(@RequestParam(required = false) Integer pageNo,
                                        @RequestParam(required = false) Integer perPage) {

        Integer[] pageAndPager = this.standardPager(pageNo, perPage);

        return productService.findAll(pageAndPager[0], pageAndPager[1]);
    }

    @GetMapping("/search")
    public Page<Product> findByShortNameContaining(@RequestParam String searchName,
                                                   @RequestParam(required = false) Integer pageNo,
                                                   @RequestParam(required = false) Integer perPage) {

        Integer[] pageAndPager = this.standardPager(pageNo, perPage);

        return productService.findByShortNameContaining(searchName, pageAndPager[0], pageAndPager[1]);
    }

    @PostMapping("/create")
    public Product createNewProduct(@RequestBody ProductCreateRequest productCreateRequest, HttpServletRequest request) {
        return productService.createNewProduct(productCreateRequest, request);
    }

    @DeleteMapping("/delete")
    public void deleteProduct(@RequestParam Long id) {
        productService.deleteProduct(id);
    }

    @PostMapping("/change-enable")
    void changeEnabled(@RequestParam Long id) {
        productService.changeEnabled(id);
    }

    @GetMapping("/category")
    Page<Product> filterByCategory(@RequestParam Long categoryId,
                                   @RequestParam(required = false) Integer pageNo,
                                   @RequestParam(required = false) Integer perPage) {
        Integer[] pageAndPager = this.standardPager(pageNo, perPage);
        return productService.filterByCategory(categoryId, pageAndPager[0], pageAndPager[1]);
    }

    @PostMapping("/update-data")
    void updateData(@RequestBody ProductUpdateDataRequest productUpdateDataRequest) {

    }

    @GetMapping("/id")
    public String getPrice(@RequestParam Long id) {
        return productService.getPrice(id);
    }


    private Integer[] standardPager(Integer pageNo, Integer perPage) {
        // 0 value is page number. If not set, = FIRST_PAGE_NO
        // 1 value is items per page. If not st, = ITEMS_PER_PAGE_STANDARD

        Integer[] pageAndPager = new Integer[2];

        if (pageNo == null) {
            pageNo = FIRST_PAGE_NO;
        }

        if (perPage == null) {
            perPage = ITEMS_PER_PAGE_STANDARD;
        }

        pageAndPager[0] = pageNo;
        pageAndPager[1] = perPage;

        return pageAndPager;
    }

}
