package org.sorus.onesixteenfour.injectors;

import net.minecraft.client.KeyboardListener;
import org.lwjgl.system.CallbackI;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.Modify;

@Hook("net/minecraft/client/KeyboardListener")
public class KeyboardListenerInjector extends Injector<KeyboardListener> {

    public KeyboardListenerInjector(KeyboardListener that) {
        super(that);
    }

    @Modify(name = "onKeyEvent", desc = "(JIIII)V")
    public static void modifyOnKeyEvent(MethodNode methodNode) {
        InsnList insnList = new InsnList();
        insnList.add(new VarInsnNode(Opcodes.ILOAD, 3));
        insnList.add(new VarInsnNode(Opcodes.ILOAD, 4));
        insnList.add(new VarInsnNode(Opcodes.ILOAD, 5));
        insnList.add(new VarInsnNode(Opcodes.ILOAD, 6));
        insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, KeyboardListenerHook.class.getName().replace(".", "/"), "onKeyEvent", "(IIII)V", false));
        methodNode.instructions.insert(insnList);
    }

}
