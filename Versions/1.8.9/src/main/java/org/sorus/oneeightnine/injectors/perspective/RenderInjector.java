package org.sorus.oneeightnine.injectors.perspective;

import net.minecraft.client.renderer.entity.Render;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.Modify;

@Hook("net/minecraft/client/renderer/entity/Render")
public class RenderInjector extends Injector<Render<?>> {

    public RenderInjector(Render<?> that) {
        super(that);
    }

    @Modify(name = "renderLivingLabel", desc = "(Lnet/minecraft/entity/Entity;Ljava/lang/String;DDDI)V")
    public static void modifyRenderLivingLabel(MethodNode methodNode) {
        int i = 0;
        for(AbstractInsnNode node : methodNode.instructions.toArray()) {
            if(node instanceof FieldInsnNode && ((FieldInsnNode) node).name.equals("renderManager")) {
                switch(i) {
                    case 1: {
                        AbstractInsnNode node1 = node.getPrevious().getPrevious();
                        methodNode.instructions.remove(node.getPrevious());
                        methodNode.instructions.remove(node.getNext());
                        methodNode.instructions.remove(node);
                        methodNode.instructions.insert(node1, new MethodInsnNode(Opcodes.INVOKESTATIC, RenderHook.class.getName().replace(".", "/"), "getRotationYaw", "()F", false));
                        break;
                    }
                    case 2: {
                        AbstractInsnNode node1 = node.getPrevious().getPrevious();
                        methodNode.instructions.remove(node.getPrevious());
                        methodNode.instructions.remove(node.getNext());
                        methodNode.instructions.remove(node);
                        methodNode.instructions.insert(node1, new MethodInsnNode(Opcodes.INVOKESTATIC, RenderHook.class.getName().replace(".", "/"), "getRotationPitch", "()F", false));
                        break;
                    }
                }
                i++;
            }
        }
    }

}
