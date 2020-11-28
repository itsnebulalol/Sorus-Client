package org.sorus.oneeightnine.injectors.optimisations;

import net.minecraft.client.multiplayer.ChunkProviderClient;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.sorus.client.obfuscation.ObfuscationManager;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.Modify;

@Hook("net/minecraft/client/multiplayer/ChunkProviderClient")
public class ChunkProviderClientInjector extends Injector<ChunkProviderClient> {

    public ChunkProviderClientInjector(ChunkProviderClient that) {
        super(that);
    }

    @Modify(name = "unloadQueuedChunks", desc = "()Z")
    public static void modifyUnloadQueuedChunks(MethodNode methodNode) {
        String chunkListingClass = ObfuscationManager.getClassName("net/minecraft/client/multiplayer/ChunkProviderClient");
        String chunkListingField = ObfuscationManager.getFieldName("net/minecraft/client/multiplayer/ChunkProviderClient", "chunkListing");
        for(AbstractInsnNode node : methodNode.instructions.toArray()) {
            if(node instanceof FieldInsnNode && ((FieldInsnNode) node).owner.equals(chunkListingClass) && ((FieldInsnNode) node).name.equals(chunkListingField)) {
                methodNode.instructions.insert(node, new MethodInsnNode(Opcodes.INVOKESTATIC, ChunkProviderClientHook.class.getName().replace(".", "/"), "newList", "(Ljava/util/List;)Ljava/util/List;", false));
            }
        }
    }

}
