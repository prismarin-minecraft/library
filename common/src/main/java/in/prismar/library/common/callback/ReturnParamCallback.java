package in.prismar.library.common.callback;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public interface ReturnParamCallback<T, K> {

    T call(K param);
}
