

package org.sorus.oneeightnine.injectors.perspective;

import net.minecraft.client.renderer.EntityRenderer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.sorus.client.obfuscation.ObfuscationManager;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.Modify;

@Hook("net/minecraft/client/renderer/EntityRenderer")
public class EntityRendererInjector extends Injector<EntityRenderer> {

    public EntityRendererInjector(EntityRenderer that) {
        super(that);
    }

    @Modify(name = "updateCameraAndRender", desc = "(FJ)V")
    public static void transformUpdateCameraAndRender(MethodNode methodNode) {
        String setAnglesClass = ObfuscationManager.getClassName("net/minecraft/client/entity/EntityPlayerSP");
        String setAnglesMethod = ObfuscationManager.getMethodName("net/minecraft/entity/Entity", "setAngles", "(FF)V");
        for(AbstractInsnNode node : methodNode.instructions.toArray()) {
            if(node instanceof MethodInsnNode && ((MethodInsnNode) node).owner.equals(setAnglesClass) && ((MethodInsnNode) node).name.equals(setAnglesMethod)) {
                AbstractInsnNode node1 = node.getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious();
                methodNode.instructions.remove(node1.getPrevious().getPrevious());
                methodNode.instructions.remove(node1.getPrevious());
                methodNode.instructions.remove(node1);
                AbstractInsnNode node2 = node.getPrevious();
                methodNode.instructions.insert(node2, new MethodInsnNode(Opcodes.INVOKESTATIC, EntityRendererHook.class.getName().replace(".", "/"), "setAngles", "(FF)V", false));
                methodNode.instructions.remove(node);
            }
        }
    }

    @Modify(name = "orientCamera", desc = "(F)V")
    public static void transformOrientCamera(MethodNode methodNode) {
        EntityRendererInjector.transform(methodNode);
    }

    private static void transform(MethodNode methodNode) {
        for(AbstractInsnNode node : methodNode.instructions.toArray()) {
            String rotationYawField = ObfuscationManager.getFieldName("net/minecraft/entity/Entity", "rotationYaw");
            String prevRotationYawField = ObfuscationManager.getFieldName("net/minecraft/entity/Entity", "prevRotationYaw");
            String rotationPitchField = ObfuscationManager.getFieldName("net/minecraft/entity/Entity", "rotationPitch");
            String prevRotationPitchField = ObfuscationManager.getFieldName("net/minecraft/entity/Entity", "prevRotationPitch");
            if(node instanceof FieldInsnNode && (((FieldInsnNode) node).name.equals(rotationYawField) || ((FieldInsnNode) node).name.equals(prevRotationYawField))) {
                AbstractInsnNode nextNode = node.getNext();
                methodNode.instructions.remove(node.getPrevious());
                methodNode.instructions.remove(node);
                methodNode.instructions.insertBefore(nextNode, new MethodInsnNode(Opcodes.INVOKESTATIC, EntityRendererHook.class.getCanonicalName().replace(".", "/"), "getRotationYaw", "()F", false));
            }
            if(node instanceof FieldInsnNode && (((FieldInsnNode) node).name.equals(rotationPitchField) || ((FieldInsnNode) node).name.equals(prevRotationPitchField))) {
                AbstractInsnNode nextNode = node.getNext();
                methodNode.instructions.remove(node.getPrevious());
                methodNode.instructions.remove(node);
                methodNode.instructions.insertBefore(nextNode, new MethodInsnNode(Opcodes.INVOKESTATIC, EntityRendererHook.class.getCanonicalName().replace(".", "/"), "getRotationPitch", "()F", false));
            }
        }
    }

}
