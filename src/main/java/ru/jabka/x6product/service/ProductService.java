package ru.jabka.x6product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import ru.jabka.x6product.exception.BadRequestException;
import ru.jabka.x6product.model.Product;
import ru.jabka.x6product.model.ProductExists;
import ru.jabka.x6product.repository.ProductRepository;

import java.util.Set;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(rollbackFor = Throwable.class)
    public Product create(final Product product) {
        validateProduct(product);
        return productRepository.insert(product);
    }

    @Transactional(rollbackFor = Throwable.class)
    public Product update(final Product product) {
        validateProduct(product);
        return productRepository.update(product);
    }

    @Transactional(rollbackFor = Throwable.class)
    public Product getById(Long id) {
        Product product = productRepository.getById(id);
        productRepository.setUsages(id);
        return product;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "productExists", key = "#ids.stream().sorted().toArray()")
    public ProductExists isProductsExists(Set<Long> ids) {
        validateProductListForExistence(ids);
        return new ProductExists(productRepository.isExists(ids));
    }

    private void validateProduct(final Product product) {
        ofNullable(product).orElseThrow(() -> new BadRequestException("Введите информацию о товаре"));
        if (!StringUtils.hasText(product.name())) {
            throw new BadRequestException("Укажите наименование товара!");
        }
        ofNullable(product.price()).orElseThrow(() -> new BadRequestException("Заполните цену товара"));
        if (product.price() < 0) {
            throw new BadRequestException("Цена не может быть отрицательным значением");
        }
    }

    private void validateProductListForExistence(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new BadRequestException("Список продуктов для проверки наличия пуст!");
        }
    }
}