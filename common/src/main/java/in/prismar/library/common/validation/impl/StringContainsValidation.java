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
public class StringContainsValidation implements Validation {

    private String content;

    @Override
    public ValidationType getType() {
        return ValidationType.STRING_CONTAINS;
    }

    @Override
    public boolean validate(String input) {
        return input.toLowerCase().contains(content.toLowerCase());
    }

}
