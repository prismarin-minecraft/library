package in.prismar.library.messaging.listener;

import in.prismar.library.messaging.packet.Packet;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public interface PacketListener<T extends Packet> {

    void onReceive(T packet);
}
