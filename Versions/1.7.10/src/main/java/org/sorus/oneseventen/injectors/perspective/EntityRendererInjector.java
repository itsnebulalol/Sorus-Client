

package org.sorus.oneseventen.injectors.perspective;

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

    @Modify(name = "updateCameraAndRender", desc = "(F)V")
    public static void transformUpdateCameraAndRender(MethodNode methodNode) {
        for(AbstractInsnNode node : methodNode.instructions.toArray()) {
            if(node.getOpcode() == Opcodes.IFEQ && node.getPrevious() instanceof VarInsnNode && node.getPrevious().getOpcode() == Opcodes.ILOAD) {
                InsnList insnList = new InsnList();
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, EntityRendererHook.class.getCanonicalName().replace(".", "/"), "overrideMouse", "()Z", false));
                insnList.add(new JumpInsnNode(Opcodes.IFEQ, ((JumpInsnNode) node).label));
                methodNode.instructions.insert(node, insnList);
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
