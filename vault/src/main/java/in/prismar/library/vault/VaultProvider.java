package in.prismar.library.vault;

import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public interface VaultProvider {

    @Nullable
    String getSecret(String category, String key);
}
