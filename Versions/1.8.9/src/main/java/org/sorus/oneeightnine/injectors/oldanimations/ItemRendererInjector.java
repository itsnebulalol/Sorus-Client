

package org.sorus.oneeightnine.injectors.oldanimations;

import net.minecraft.client.renderer.ItemRenderer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.sorus.client.Sorus;
import org.sorus.client.module.impl.oldanimations.OldAnimations;
import org.sorus.client.obfuscation.ObfuscationManager;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.Modify;

@Hook("net/minecraft/client/renderer/ItemRenderer")
public class ItemRendererInjector extends Injector<ItemRenderer> {

    public ItemRendererInjector(ItemRenderer that) {
        super(that);
    }

    @Modify(name = "renderItemInFirstPerson", desc = "(F)V")
    public static void transform(MethodNode methodNode) {
        int i = 0;
        for(AbstractInsnNode node : methodNode.instructions.toArray()) {
            if(node instanceof MethodInsnNode) {
                String methodName = ObfuscationManager.getMethodName("net/minecraft/client/renderer/ItemRenderer", "transformFirstPersonItem", "(FF)V");
                if(((MethodInsnNode) node).name.equals(methodName) && ((MethodInsnNode) node).desc.equals("(FF)V") && i < 4) {
                    methodNode.instructions.remove(node.getPrevious());
                    methodNode.instructions.insertBefore(node, new VarInsnNode(Opcodes.FLOAD, 4));
                    methodNode.instructions.insertBefore(node, new MethodInsnNode(Opcodes.INVOKESTATIC, ItemRendererInjector.class.getCanonicalName().replace(".", "/"), "getAnimation", "(F)F", false));
                    i++;
                }
            }
        }
    }

    public static float getAnimation(float animation) {
        return Sorus.getSorus().getModuleManager().getModule(OldAnimations.class).shouldShowAnimInteractBreak() ? animation : 0;
    }

}
