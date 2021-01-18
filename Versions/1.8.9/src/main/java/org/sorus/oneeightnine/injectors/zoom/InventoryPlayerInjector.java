package org.sorus.oneeightnine.injectors.zoom;

import net.minecraft.entity.player.InventoryPlayer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.Modify;

@Hook("net/minecraft/entity/player/InventoryPlayer")
public class InventoryPlayerInjector extends Injector<InventoryPlayer> {

    public InventoryPlayerInjector(InventoryPlayer that) {
        super(that);
    }

    @Modify(name = "changeCurrentItem", desc = "(I)V")
    public static void modifyChangeCurrentItem(MethodNode methodNode) {
        InsnList insnList = new InsnList();
        insnList.add(new VarInsnNode(Opcodes.ILOAD, 1));
        insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, InventoryPlayerHook.class.getName().replace(".", "/"), "getCurrentItemChange", "(I)I", false));
        insnList.add(new VarInsnNode(Opcodes.ISTORE, 1));
        methodNode.instructions.insert(insnList);
    }

}
