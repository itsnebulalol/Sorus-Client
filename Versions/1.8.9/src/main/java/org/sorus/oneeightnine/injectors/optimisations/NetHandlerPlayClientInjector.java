package org.sorus.oneeightnine.injectors.optimisations;

import net.minecraft.client.network.NetHandlerPlayClient;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.sorus.client.obfuscation.ObfuscationManager;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.Modify;

@Hook("net/minecraft/client/network/NetHandlerPlayClient")
public class NetHandlerPlayClientInjector extends Injector<NetHandlerPlayClient> {

    public NetHandlerPlayClientInjector(NetHandlerPlayClient that) {
        super(that);
    }

    @Modify(name = "handleChunkData", desc = "(Lnet/minecraft/network/play/server/S21PacketChunkData;)V")
    public static void modifyHandleChunkData(MethodNode methodNode) {
        String checkThreadAndEnqueueClass = ObfuscationManager.getClassName("net/minecraft/network/PacketThreadUtil");
        String checkThreadAndEnqueueMethod = ObfuscationManager.getMethodName("net/minecraft/network/PacketThreadUtil", "checkThreadAndEnqueue", "(Lnet/minecraft/network/Packet;Lnet/minecraft/network/INetHandler;Lnet/minecraft/util/IThreadListener;)V");
        for(AbstractInsnNode node : methodNode.instructions.toArray()) {
            if(node instanceof MethodInsnNode && ((MethodInsnNode) node).owner.equals(checkThreadAndEnqueueClass) && ((MethodInsnNode) node).name.equals(checkThreadAndEnqueueMethod)) {
                methodNode.instructions.remove(node.getPrevious().getPrevious().getPrevious());
                methodNode.instructions.remove(node.getPrevious().getPrevious());
                methodNode.instructions.remove(node.getPrevious());
                methodNode.instructions.remove(node);
            }
        }
    }

}
