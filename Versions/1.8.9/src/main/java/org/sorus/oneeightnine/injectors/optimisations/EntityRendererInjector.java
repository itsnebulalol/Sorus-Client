package org.sorus.oneeightnine.injectors.optimisations;

import net.minecraft.client.renderer.EntityRenderer;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.sorus.client.obfuscation.ObfuscationManager;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.Modify;

@Hook("net/minecraft/client/renderer/EntityRenderer")
public class EntityRendererInjector extends Injector<EntityRenderer> {

    public EntityRendererInjector(EntityRenderer that) {
        super(that);
    }

    @Modify(name = "renderWorldPass", desc = "(IFJ)V")
    public static void transformRenderWorldPass(MethodNode methodNode) {
        String getInstanceClass = ObfuscationManager.getClassName("net/minecraft/client/renderer/culling/ClippingHelperImpl");
        String getInstanceMethod = ObfuscationManager.getMethodName("net/minecraft/client/renderer/culling/ClippingHelperImpl", "getInstance", "()Lnet/minecraft/client/renderer/culling/ClippingHelperImpl;");
        for(AbstractInsnNode node : methodNode.instructions.toArray()) {
            if(node instanceof MethodInsnNode && ((MethodInsnNode) node).owner.equals(getInstanceClass) && ((MethodInsnNode) node).name.equals(getInstanceMethod)) {
                methodNode.instructions.remove(node.getNext());
                methodNode.instructions.remove(node);
            }
        }
    }

}
