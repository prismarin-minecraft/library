package in.prismar.library.common.validation.impl;

import in.prismar.library.common.validation.Validation;
import in.prismar.library.common.validation.ValidationType;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Copyright (c) ReaperMaga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by ReaperMaga
 **/
@Getter
@AllArgsConstructor
public class MinMaxValidation implements Validation {

    private final int min;
    private final int max;

    @Override
    public ValidationType getType() {
        return ValidationType.MIN_MAX;
    }

    @Override
    public boolean validate(String input) {
        int length = input.toCharArray().length;
        if(length >= min && length <= max) {
            return true;
        }
        return false;
    }

}
