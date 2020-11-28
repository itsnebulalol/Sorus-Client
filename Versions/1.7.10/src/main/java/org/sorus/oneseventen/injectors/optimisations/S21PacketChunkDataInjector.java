package org.sorus.oneseventen.injectors.optimisations;

import net.minecraft.network.play.server.S21PacketChunkData;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.Modify;

@Hook("net/minecraft/network/play/server/S21PacketChunkData")
public class S21PacketChunkDataInjector extends Injector<S21PacketChunkData> {

    public S21PacketChunkDataInjector(S21PacketChunkData that) {
        super(that);
    }

    @Modify(name = "processPacket", desc = "(Lnet/minecraft/network/play/server/INetHandlerPlayClient;)V")
    public static void modifyProcessPacket(MethodNode methodNode) {
        methodNode.instructions.remove(methodNode.instructions.getFirst());
        InsnList insnList = new InsnList();
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
        insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, S21PacketChunkDataHook.class.getName().replace(".", "/"), "processPacket", "(Lnet/minecraft/network/player/server/S21PacketChunkData;Lnet/minecraft/network/play/server/INetHandlerPlayClient;)V", false));
        methodNode.instructions.insert(insnList);
    }

}
