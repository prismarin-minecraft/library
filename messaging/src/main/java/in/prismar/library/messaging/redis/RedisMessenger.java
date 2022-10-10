package in.prismar.library.messaging.redis;

import in.prismar.library.messaging.Messenger;
import in.prismar.library.messaging.listener.PacketListener;
import in.prismar.library.messaging.packet.Packet;
import org.redisson.api.RTopic;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class RedisMessenger implements Messenger {

    private final RedisMessengerConnection connection;
    private final String topicName;
    private final RTopic topic;

    public RedisMessenger(RedisMessengerConnection connection, String topicName) {
        this.connection = connection;
        this.topicName = topicName;
        this.topic = connection.getClient().getTopic(topicName);
    }

    @Override
    public <T extends Packet> void sendPacket(T packet) {
        topic.publish(packet);
    }

    @Override
    public <T extends Packet> void sendPacketAsync(T packet) {
        topic.publishAsync(packet);
    }

    @Override
    public void addListener(PacketListener<Packet> listener) {
        topic.addListener(Packet.class, (charSequence, packet) -> {
            listener.onReceive(packet);
        });
    }
}
