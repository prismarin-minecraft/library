package in.prismar.library.messaging.redis;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.codec.Kryo5Codec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@RequiredArgsConstructor
@Getter
public class RedisMessengerConnection {

    private final String address;
    private final String password;

    private RedissonClient client;


    public void connect() {
        Config config = new Config();
        config.setCodec(new JsonJacksonCodec());

        SingleServerConfig serverConfig = config.useSingleServer();
        serverConfig.setAddress(address);
        serverConfig.setPassword(password);
        this.client = Redisson.create(config);
    }

    public void disconnect() {
        if(!client.isShutdown()) {
            client.shutdown();
        }
    }


}
