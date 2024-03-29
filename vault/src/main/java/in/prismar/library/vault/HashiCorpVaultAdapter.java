package in.prismar.library.vault;

import com.bettercloud.vault.Vault;
import com.bettercloud.vault.VaultConfig;
import com.bettercloud.vault.VaultException;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class HashiCorpVaultAdapter implements VaultProvider {

    private final String path;
    private Vault vault;
    private VaultConfig config;

    public HashiCorpVaultAdapter(String address, String token, String path) throws Exception {
        this.path = path;
        this.config = new VaultConfig()
                .address(address)
                .token(token)
                .build();
        this.vault = new Vault(config, 2);
    }

    @Override
    @Nullable
    public String getSecret(String category, String key) {
        Map<String, String> secrets = getStore(category);
        if(secrets != null) {
            return secrets.get(key);
        }
        return null;
    }

    @Override
    @Nullable
    public String setSecret(String category, String key, String value) {
        Map<String, Object> map = new HashMap<>();
        Map<String, String> store = getStore(category);
        if(store != null) {
            map.putAll(store);
        }
        map.put(key, value);
        try {
            this.vault.logical().write(path.concat(category), map);
        } catch (VaultException e) {
            return null;
        }
        return value;
    }

    @Nullable
    public Map<String, String> getStore(String category) {
        try {
            return this.vault.logical().read(path.concat(category)).getData();
        }catch (Exception exception) {}
        return null;
    }
}
