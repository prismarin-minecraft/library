package in.prismar.library.common.tuple;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Data
@AllArgsConstructor
public class ImmutableTuple<T, K> {

    private final T first;
    private final T second;
}
