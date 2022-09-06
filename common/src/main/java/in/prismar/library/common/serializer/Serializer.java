package in.prismar.library.common.serializer;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public interface Serializer<S, O> {

    S serialize(O value);
    O deserialize(S serializedValue);
}
