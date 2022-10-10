package in.prismar.library.messaging;

import in.prismar.library.messaging.listener.PacketListener;
import in.prismar.library.messaging.packet.Packet;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public interface Messenger {

    <T extends Packet> void sendPacket(T packet);

    <T extends Packet> void sendPacketAsync(T packet);

    void addListener(PacketListener<Packet> listener);
}
