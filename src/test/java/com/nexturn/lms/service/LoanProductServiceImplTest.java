package com.nexturn.lms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nexturn.lms.entity.LoanProduct;
import com.nexturn.lms.exception.ResourceNotFoundException;
import com.nexturn.lms.repository.LoanProductRepository;

@ExtendWith(MockitoExtension.class)
class LoanProductServiceImplTest {

    @Mock
    private LoanProductRepository productRepository;

    @InjectMocks
    private LoanProductServiceImpl productService;

    private LoanProduct product;

    @BeforeEach
    void setUp() {

        product = new LoanProduct();

        product.setProductId(1);
        product.setMinAmount(
                new BigDecimal("10000")
        );
        product.setMaxAmount(
                new BigDecimal("500000")
        );
        product.setMinTenureMonths(6);
        product.setMaxTenureMonths(60);
        product.setDefaultInterestRate(
                new BigDecimal("10.5")
        );
        product.setPenaltyType("DAILY");
        product.setPenaltyValue(
                new BigDecimal("100")
        );
        product.setIsActive(true);
    }

    @Test
    void create_shouldSaveProduct() {

        when(productRepository.save(product))
                .thenReturn(product);

        LoanProduct result =
                productService.create(product);

        assertNotNull(result);

        assertEquals(
                1,
                result.getProductId()
        );

        assertEquals(
                new BigDecimal("10000"),
                result.getMinAmount()
        );

        assertEquals(
                new BigDecimal("500000"),
                result.getMaxAmount()
        );

        assertEquals(
                6,
                result.getMinTenureMonths()
        );

        assertEquals(
                60,
                result.getMaxTenureMonths()
        );

        assertEquals(
                true,
                result.getIsActive()
        );

        verify(productRepository)
                .save(product);
    }

    @Test
    void create_shouldSetActiveTrueWhenIsActiveIsNull() {

        LoanProduct newProduct =
                new LoanProduct();

        newProduct.setProductId(2);
        newProduct.setMinAmount(
                new BigDecimal("20000")
        );
        newProduct.setMaxAmount(
                new BigDecimal("300000")
        );
        newProduct.setMinTenureMonths(12);
        newProduct.setMaxTenureMonths(48);
        newProduct.setDefaultInterestRate(
                new BigDecimal("11.0")
        );
        newProduct.setIsActive(null);

        when(productRepository.save(newProduct))
                .thenReturn(newProduct);

        LoanProduct result =
                productService.create(newProduct);

        assertEquals(
                true,
                result.getIsActive()
        );

        verify(productRepository)
                .save(newProduct);
    }

    @Test
    void create_shouldKeepActiveFalseWhenProvided() {

        LoanProduct inactiveProduct =
                new LoanProduct();

        inactiveProduct.setProductId(3);
        inactiveProduct.setMinAmount(
                new BigDecimal("10000")
        );
        inactiveProduct.setMaxAmount(
                new BigDecimal("100000")
        );
        inactiveProduct.setMinTenureMonths(6);
        inactiveProduct.setMaxTenureMonths(24);
        inactiveProduct.setDefaultInterestRate(
                new BigDecimal("12.0")
        );
        inactiveProduct.setIsActive(false);

        when(productRepository.save(inactiveProduct))
                .thenReturn(inactiveProduct);

        LoanProduct result =
                productService.create(inactiveProduct);

        assertEquals(
                false,
                result.getIsActive()
        );

        verify(productRepository)
                .save(inactiveProduct);
    }

    @Test
    void getAllActive_shouldReturnOnlyActiveProducts() {

        LoanProduct inactiveProduct =
                new LoanProduct();

        inactiveProduct.setProductId(2);
        inactiveProduct.setIsActive(false);

        LoanProduct nullActiveProduct =
                new LoanProduct();

        nullActiveProduct.setProductId(3);
        nullActiveProduct.setIsActive(null);

        when(productRepository.findAll())
                .thenReturn(
                        List.of(
                                product,
                                inactiveProduct,
                                nullActiveProduct
                        )
                );

        List<LoanProduct> result =
                productService.getAllActive();

        assertNotNull(result);

        assertEquals(
                1,
                result.size()
        );

        assertEquals(
                1,
                result.get(0).getProductId()
        );

        assertEquals(
                true,
                result.get(0).getIsActive()
        );

        verify(productRepository)
                .findAll();
    }

    @Test
    void getAllActive_shouldReturnEmptyListWhenNoActiveProductsExist() {

        LoanProduct inactiveProduct =
                new LoanProduct();

        inactiveProduct.setProductId(2);
        inactiveProduct.setIsActive(false);

        when(productRepository.findAll())
                .thenReturn(
                        List.of(inactiveProduct)
                );

        List<LoanProduct> result =
                productService.getAllActive();

        assertNotNull(result);

        assertEquals(
                0,
                result.size()
        );

        verify(productRepository)
                .findAll();
    }

    @Test
    void getById_shouldReturnProduct() {

        when(productRepository.findById(1))
                .thenReturn(Optional.of(product));

        LoanProduct result =
                productService.getById(1);

        assertNotNull(result);

        assertEquals(
                1,
                result.getProductId()
        );

        assertEquals(
                new BigDecimal("500000"),
                result.getMaxAmount()
        );

        verify(productRepository)
                .findById(1);
    }

    @Test
    void getById_shouldThrowExceptionWhenProductDoesNotExist() {

        when(productRepository.findById(999))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> productService.getById(999)
        );

        verify(productRepository)
                .findById(999);
    }
}