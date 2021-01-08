package org.sorus.oneeightnine.injectors;

import net.minecraft.client.renderer.entity.RenderManager;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.sorus.client.obfuscation.ObfuscationManager;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.Modify;

@Hook("net/minecraft/client/renderer/entity/RenderManager")
public class RenderManagerInjector extends Injector<RenderManager> {

    public RenderManagerInjector(RenderManager that) {
        super(that);
    }

    @Modify(name = "doRenderEntity", desc = "(Lnet/minecraft/entity/Entity;DDDFFZ)Z")
    public static void modifyDoRender(MethodNode methodNode) {
        String entityClass = ObfuscationManager.getClassName("net/minecraft/entity/Entity");
        InsnList insnList = new InsnList();
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
        insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, RenderManagerHook.class.getName().replace(".", "/"), "onRenderEntity", "(L" + entityClass + ";)V", false));
        methodNode.instructions.insert(insnList);
    }

}
