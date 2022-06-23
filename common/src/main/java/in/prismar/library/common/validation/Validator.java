package in.prismar.library.common.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) ReaperMaga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by ReaperMaga
 **/
@Getter
@AllArgsConstructor
public class Validator {

    private List<Validation> validations;
    private String[] inputs;

    public Validator(String... input) {
        this.inputs = input;
        this.validations = new ArrayList<>();
    }

    public Validator addValidation(Validation validation) {
        this.validations.add(validation);
        return this;
    }

    public ValidationResult validate() {
        for(String input : inputs) {
            for(Validation validation : validations) {
                if(!validation.validate(input)) {
                    return new ValidationResult(false, validation.getType());
                }
            }
        }
        return new ValidationResult(true, null);
    }
}
