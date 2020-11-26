package org.sorus.oneeightnine.injectors.optimisations;

import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.S21PacketChunkData;

public class S21PacketChunkDataHook {

    public static void processPacket(S21PacketChunkData packet, INetHandlerPlayClient handler) {
        new Thread(() -> handler.handleChunkData(packet));
    }

}
