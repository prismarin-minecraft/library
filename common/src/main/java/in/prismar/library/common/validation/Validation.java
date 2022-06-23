package in.prismar.library.common.validation;

/**
 * Copyright (c) ReaperMaga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by ReaperMaga
 **/
public interface Validation {

    ValidationType getType();
    boolean validate(String input);
}
