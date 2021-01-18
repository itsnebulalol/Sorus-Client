package org.sorus.oneeightnine.injectors.perspective;

import net.minecraft.client.renderer.RenderGlobal;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.sorus.client.obfuscation.ObfuscationManager;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.Modify;

@Hook("net/minecraft/client/renderer/RenderGlobal")
public class RenderGlobalInjector extends Injector<RenderGlobal> {

    public RenderGlobalInjector(RenderGlobal that) {
        super(that);
    }

    @Modify(name = "setupTerrain", desc = "(Lnet/minecraft/entity/Entity;DLnet/minecraft/client/renderer/culling/ICamera;IZ)V")
    public static void modifySetupTerrain(MethodNode methodNode) {
        for(AbstractInsnNode node : methodNode.instructions.toArray()) {
            String parentClass = ObfuscationManager.getClassName("net/minecraft/entity/Entity");
            String rotationYawField = ObfuscationManager.getFieldName("net/minecraft/entity/Entity", "rotationYaw");
            String prevRotationYawField = ObfuscationManager.getFieldName("net/minecraft/entity/Entity", "prevRotationYaw");
            String rotationPitchField = ObfuscationManager.getFieldName("net/minecraft/entity/Entity", "rotationPitch");
            String prevRotationPitchField = ObfuscationManager.getFieldName("net/minecraft/entity/Entity", "prevRotationPitch");
            if(node instanceof FieldInsnNode && ((FieldInsnNode) node).owner.equals(parentClass) && (((FieldInsnNode) node).name.equals(rotationYawField) || ((FieldInsnNode) node).name.equals(prevRotationYawField))) {
                AbstractInsnNode nextNode = node.getNext();
                methodNode.instructions.remove(node.getPrevious());
                methodNode.instructions.remove(node);
                methodNode.instructions.insertBefore(nextNode, new MethodInsnNode(Opcodes.INVOKESTATIC, EntityRendererHook.class.getCanonicalName().replace(".", "/"), "getRotationYaw", "()F", false));
            }
            if(node instanceof FieldInsnNode && ((FieldInsnNode) node).owner.equals(parentClass) && (((FieldInsnNode) node).name.equals(rotationPitchField) || ((FieldInsnNode) node).name.equals(prevRotationPitchField))) {
                AbstractInsnNode nextNode = node.getNext();
                methodNode.instructions.remove(node.getPrevious());
                methodNode.instructions.remove(node);
                methodNode.instructions.insertBefore(nextNode, new MethodInsnNode(Opcodes.INVOKESTATIC, EntityRendererHook.class.getCanonicalName().replace(".", "/"), "getRotationPitch", "()F", false));
            }
        }
    }

}
