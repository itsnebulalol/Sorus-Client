

package org.sorus.oneseventen.injectors.enhancements;

import net.minecraft.client.renderer.InventoryEffectRenderer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.sorus.client.Sorus;
import org.sorus.client.module.impl.enhancements.Enhancements;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.Modify;

@Hook("net/minecraft/client/renderer/InventoryEffectRenderer")
public class InventoryEffectRendererInjector extends Injector<InventoryEffectRenderer> {

    public InventoryEffectRendererInjector(InventoryEffectRenderer that) {
        super(that);
    }

    /*@Modify(name = "drawActivePotionEffects")
    public static void modifyUpdateActivePotionEffects(MethodNode methodNode) {
        LabelNode labelNode = new LabelNode();
        for(AbstractInsnNode node : methodNode.instructions.toArray()) {
            if(node instanceof VarInsnNode && node.getNext() instanceof IntInsnNode) {
                InsnList insnList = new InsnList();
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, InventoryEffectRendererInjector.class.getCanonicalName().replace(".", "/"), "shouldShiftInventory", "()Z", false));
                insnList.add(new JumpInsnNode(Opcodes.IFEQ, labelNode));
                methodNode.instructions.insertBefore(node, insnList);
            }
            if(node instanceof LineNumberNode && node.getNext() instanceof VarInsnNode && node.getNext().getNext() instanceof InsnNode && node.getNext().getNext().getOpcode() == 4) {
                methodNode.instructions.insertBefore(node, labelNode);
            }
        }
    }*/

    public static boolean shouldShiftInventory() {
        Enhancements enhancements = Sorus.getSorus().getModuleManager().getModule(Enhancements.class);
        return enhancements.isEnabled() && !enhancements.shouldCenterInventory();
    }

}
