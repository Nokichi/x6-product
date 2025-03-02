package ru.jabka.x6product.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.jabka.x6product.model.Product;
import ru.jabka.x6product.model.ProductExists;
import ru.jabka.x6product.service.ProductService;

@RestController
@Tag(name = "Товары")
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public Product create(@RequestBody final Product product) {
        return productService.create(product);
    }

    @PatchMapping
    public Product update(@RequestBody final Product product) {
        return productService.update(product);
    }

    @GetMapping("/{id}")
    public Product get(@PathVariable final Long id) {
        return productService.getById(id);
    }

    @GetMapping("/exists")
    public ProductExists isProductExists(@RequestParam final Long id) {
        return productService.isProductExists(id);
    }
}