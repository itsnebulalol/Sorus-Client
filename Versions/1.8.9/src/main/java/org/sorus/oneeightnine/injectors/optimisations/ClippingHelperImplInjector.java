package org.sorus.oneeightnine.injectors.optimisations;

import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.sorus.client.obfuscation.ObfuscationManager;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.Modify;

@Hook("net/minecraft/client/renderer/culling/ClippingHelperImpl")
public class ClippingHelperImplInjector extends Injector<ClippingHelperImpl> {

    public ClippingHelperImplInjector(ClippingHelperImpl that) {
        super(that);
    }

    @Modify(name = "getInstance", desc = "()Lnet/minecraft/client/renderer/culling/ClippingHelper;")
    public static void modifyGetInstance(MethodNode methodNode) {
        String instanceClass = ObfuscationManager.getClassName("net/minecraft/client/renderer/culling/ClippingHelperImpl");
        String instanceField = ObfuscationManager.getFieldName("net/minecraft/client/renderer/culling/ClippingHelperImpl", "instance");
        InsnList insnList = new InsnList();
        insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ClippingHelperImplHook.class.getName().replace(".", "/"), "skipUpdate", "()Z", false));
        LabelNode label = new LabelNode();
        insnList.add(new JumpInsnNode(Opcodes.IFEQ, label));
        insnList.add(new FieldInsnNode(Opcodes.GETSTATIC, instanceClass, instanceField, "L" + instanceClass + ";"));
        insnList.add(new InsnNode(Opcodes.ARETURN));
        insnList.add(label);
        methodNode.instructions.insert(insnList);
    }

}
