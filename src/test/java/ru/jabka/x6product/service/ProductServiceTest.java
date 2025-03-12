package ru.jabka.x6product.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.jabka.x6product.exception.BadRequestException;
import ru.jabka.x6product.model.Product;
import ru.jabka.x6product.model.ProductExists;
import ru.jabka.x6product.repository.ProductRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void create_success() {
        final Product product = getValidProduct();
        Mockito.when(productRepository.insert(product)).thenReturn(product);
        Product result = productService.create(product);
        Assertions.assertEquals(product, result);
        Mockito.verify(productRepository).insert(product);
    }

    @Test
    void update_success() {
        final Product product = getValidProduct();
        Mockito.when(productRepository.update(product)).thenReturn(product);
        Product result = productService.update(product);
        Assertions.assertEquals(product, result);
        Mockito.verify(productRepository).update(product);
    }

    @Test
    void getById_success() {
        final Product product = getValidProduct();
        Mockito.when(productRepository.getById(product.id())).thenReturn(product);
        Product result = productService.getById(product.id());
        Assertions.assertEquals(product, result);
        Mockito.verify(productRepository).getById(product.id());
    }

    @Test
    void isProductExistsById_found() {
        final Product product = getValidProduct();
        Set<Long> ids = Collections.singleton(product.id());
        Map<Long, Boolean> checkList = new HashMap<>();
        checkList.put(product.id(), true);
        Mockito.when(productRepository.isExists(ids)).thenReturn(checkList);
        ProductExists productExists = new ProductExists(checkList);
        ProductExists result = productService.isProductsExists(ids);
        Assertions.assertEquals(productExists, result);
        Mockito.verify(productRepository).isExists(ids);
    }

    @Test
    void isProductExistsById_notFound() {
        long fakeId = 1L;
        Set<Long> ids = Collections.singleton(fakeId);
        Map<Long, Boolean> checkList = new HashMap<>();
        checkList.put(fakeId, false);
        Mockito.when(productRepository.isExists(ids)).thenReturn(checkList);
        ProductExists productExists = new ProductExists(checkList);
        ProductExists result = productService.isProductsExists(ids);
        Assertions.assertEquals(productExists, result);
        Mockito.verify(productRepository).isExists(ids);
    }

    @Test
    void create_error_nullProduct() {
        final Product product = null;
        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> productService.create(product)
        );
        Assertions.assertEquals(exception.getMessage(), "Введите информацию о товаре");
        Mockito.verify(productRepository, Mockito.never()).insert(Mockito.any());
    }

    @Test
    void create_error_nullName() {
        final Product product = Product.builder()
                .id(2L)
                .name(null)
                .price(160.0)
                .build();
        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> productService.create(product)
        );
        Assertions.assertEquals(exception.getMessage(), "Укажите наименование товара!");
        Mockito.verify(productRepository, Mockito.never()).insert(Mockito.any());
    }

    @Test
    void create_error_nullPrice() {
        final Product product = Product.builder()
                .id(2L)
                .name("Батончик сникерс")
                .price(null)
                .build();
        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> productService.create(product)
        );
        Assertions.assertEquals(exception.getMessage(), "Заполните цену товара");
        Mockito.verify(productRepository, Mockito.never()).insert(Mockito.any());
    }

    @Test
    void create_error_invalidPrice() {
        final Product product = Product.builder()
                .id(2L)
                .name("Батончик сникерс")
                .price(-1.0)
                .build();
        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> productService.create(product)
        );
        Assertions.assertEquals(exception.getMessage(), "Цена не может быть отрицательным значением");
        Mockito.verify(productRepository, Mockito.never()).insert(Mockito.any());
    }

    @Test
    void update_error_nullProduct() {
        final Product product = null;
        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> productService.update(product)
        );
        Assertions.assertEquals(exception.getMessage(), "Введите информацию о товаре");
        Mockito.verify(productRepository, Mockito.never()).update(Mockito.any());
    }

    @Test
    void update_error_nullName() {
        final Product product = Product.builder()
                .id(2L)
                .name(null)
                .price(160.0)
                .build();
        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> productService.update(product)
        );
        Assertions.assertEquals(exception.getMessage(), "Укажите наименование товара!");
        Mockito.verify(productRepository, Mockito.never()).update(Mockito.any());
    }

    @Test
    void update_error_nullPrice() {
        final Product product = Product.builder()
                .id(2L)
                .name("Батончик сникерс")
                .price(null)
                .build();
        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> productService.update(product)
        );
        Assertions.assertEquals(exception.getMessage(), "Заполните цену товара");
        Mockito.verify(productRepository, Mockito.never()).update(Mockito.any());
    }

    @Test
    void update_error_invalidPrice() {
        final Product product = Product.builder()
                .id(2L)
                .name("Батончик сникерс")
                .price(-1.0)
                .build();
        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> productService.update(product)
        );
        Assertions.assertEquals(exception.getMessage(), "Цена не может быть отрицательным значением");
        Mockito.verify(productRepository, Mockito.never()).update(Mockito.any());
    }

    private Product getValidProduct() {
        return Product.builder()
                .id(1L)
                .name("Батончик сникерс")
                .price(160.0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}