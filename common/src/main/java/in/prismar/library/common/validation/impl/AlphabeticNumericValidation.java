package in.prismar.library.common.validation.impl;


import in.prismar.library.common.validation.Validation;
import in.prismar.library.common.validation.ValidationType;

/**
 * Copyright (c) ReaperMaga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by ReaperMaga
 **/
public class AlphabeticNumericValidation implements Validation {

    private String regexPattern = "^[a-zA-Z0-9]*$";

    @Override
    public ValidationType getType() {
        return ValidationType.SIMPLE_ALPHABETIC;
    }

    @Override
    public boolean validate(String input) {
        if(input.isEmpty()) {
            return false;
        }
        return input.matches(regexPattern);
    }

}
