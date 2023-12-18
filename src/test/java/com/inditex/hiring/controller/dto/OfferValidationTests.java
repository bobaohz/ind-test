package com.inditex.hiring.controller.dto;



import com.inditex.hiring.TestConstants;
import com.inditex.hiring.utils.DateUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Random;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class OfferValidationTests {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsCorrectThenValidationSucceeds() {
        Random random = new Random();
        var offer =
                new Offer(Math.abs(random.nextLong()), TestConstants.BRAND_ID_ZARA,
                        DateUtil.convertToInstant("2020-06-14T00.00.00Z"),
                        DateUtil.convertToInstant("2020-06-14T01.00.00Z"),
                        1l,"0001002",
                        0, BigDecimal.valueOf(100.99), "EUR");
        Set<ConstraintViolation<Offer>> violations = validator.validate(offer);
        assertThat(violations).isEmpty();
    }

    @Test
    void whenOfferIdIsNullThenValidationFails() {
        var offer =
                new Offer(null, TestConstants.BRAND_ID_ZARA,
                        DateUtil.convertToInstant("2020-06-14T00.00.00Z"),
                        DateUtil.convertToInstant("2020-06-14T01.00.00Z"), 1l,
                        "0001002",
                        0, BigDecimal.valueOf(100.99), "EUR");
        Set<ConstraintViolation<Offer>> violations = validator.validate(offer);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The offer id must be defined.");
    }

    @Test
    void overallValidationFails() {
        var offer =
                new Offer(null, TestConstants.BRAND_ID_ZARA,
                        DateUtil.convertToInstant("2020-06-14T00.00.00Z"),
                        DateUtil.convertToInstant("2020-06-14T01.00.00Z"), null,
                        null,
                        0, BigDecimal.valueOf(100.99), "EUR");
        Set<ConstraintViolation<Offer>> violations = validator.validate(offer);
        assertThat(violations).hasSize(3);
    }

}