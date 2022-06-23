package in.prismar.library.common;

import in.prismar.library.common.validation.ValidationResult;
import in.prismar.library.common.validation.Validator;
import in.prismar.library.common.validation.impl.AlphabeticNumericValidation;
import in.prismar.library.common.validation.impl.MinMaxValidation;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class ValidationTest {

    @Test
    public void testNonFailAlphabeticNumericValidation() {
        Validator validator = new Validator("Hello")
                .addValidation(new AlphabeticNumericValidation());

        ValidationResult result = validator.validate();
        assertEquals(true, result.isSuccess());
    }

    @Test
    public void testFailAlphabeticNumericValidation() {
        Validator validator = new Validator(".:Hello*{")
                .addValidation(new AlphabeticNumericValidation());

        ValidationResult result = validator.validate();
        assertEquals(false, result.isSuccess());
    }

    @Test
    public void testNonFailMinMaxValidation() {
        Validator validator = new Validator("Testing")
                .addValidation(new MinMaxValidation(1, 10));

        ValidationResult result = validator.validate();
        assertEquals(true, result.isSuccess());
    }

    @Test
    public void testFailMinMaxValidation() {
        Validator validator = new Validator("Testing")
                .addValidation(new MinMaxValidation(1, 3));

        ValidationResult result = validator.validate();
        assertEquals(false, result.isSuccess());
    }
}
