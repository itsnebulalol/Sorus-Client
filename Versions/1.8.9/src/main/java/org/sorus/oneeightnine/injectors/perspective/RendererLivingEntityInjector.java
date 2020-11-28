package org.sorus.oneeightnine.injectors.perspective;

import net.minecraft.client.renderer.entity.RendererLivingEntity;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.sorus.client.obfuscation.ObfuscationManager;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.Modify;

@Hook("net/minecraft/client/renderer/entity/RendererLivingEntity")
public class RendererLivingEntityInjector extends Injector<RendererLivingEntity<?>> {

    public RendererLivingEntityInjector(RendererLivingEntity<?> that) {
        super(that);
    }

    @Modify(name = "renderName", desc = "(Lnet/minecraft/entity/EntityLivingBase;DDD)V")
    public static void modifyRenderName(MethodNode methodNode) {
        String renderManagerClass = ObfuscationManager.getClassName("net/minecraft/client/renderer/entity/RendererLivingEntity");
        String renderManagerField = ObfuscationManager.getFieldName("net/minecraft/client/renderer/entity/RendererLivingEntity", "renderManager");
        int i = 0;
        for(AbstractInsnNode node : methodNode.instructions.toArray()) {
            if(node instanceof FieldInsnNode && ((FieldInsnNode) node).owner.equals(renderManagerClass) && ((FieldInsnNode) node).name.equals(renderManagerField)) {
                switch(i) {
                    case 1: {
                        AbstractInsnNode node1 = node.getPrevious().getPrevious();
                        methodNode.instructions.remove(node.getPrevious());
                        methodNode.instructions.remove(node.getNext());
                        methodNode.instructions.remove(node);
                        methodNode.instructions.insert(node1, new MethodInsnNode(Opcodes.INVOKESTATIC, RendererLivingEntityHook.class.getName().replace(".", "/"), "getRotationYaw", "()F", false));
                        break;
                    }
                    case 2: {
                        AbstractInsnNode node1 = node.getPrevious().getPrevious();
                        methodNode.instructions.remove(node.getPrevious());
                        methodNode.instructions.remove(node.getNext());
                        methodNode.instructions.remove(node);
                        methodNode.instructions.insert(node1, new MethodInsnNode(Opcodes.INVOKESTATIC, RendererLivingEntityHook.class.getName().replace(".", "/"), "getRotationPitch", "()F", false));
                        break;
                    }
                }
                i++;
            }
        }
    }

}
