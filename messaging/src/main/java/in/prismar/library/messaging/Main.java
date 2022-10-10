package in.prismar.library.messaging;

import in.prismar.library.messaging.listener.PacketListener;
import in.prismar.library.messaging.packet.CustomPayloadPacket;
import in.prismar.library.messaging.packet.Packet;
import in.prismar.library.messaging.redis.RedisMessenger;
import in.prismar.library.messaging.redis.RedisMessengerConnection;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class Main {

    public static void main(String[] args) {
        RedisMessengerConnection connection = new RedisMessengerConnection("redis://46.4.69.117:6379", "QsFZa6HRa8ygbDaS9EL6BX27K4ch6u7N");
        connection.connect();
        Messenger messenger = new RedisMessenger(connection, "test");

        messenger.addListener(new PacketListener<Packet>() {
            @Override
            public void onReceive(Packet packet) {
                System.out.println(packet.getClass().getSimpleName());
            }
        });

        messenger.sendPacket(new CustomPayloadPacket("Test"));
    }
}
