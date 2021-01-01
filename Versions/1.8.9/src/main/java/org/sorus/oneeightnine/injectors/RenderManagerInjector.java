package org.sorus.oneeightnine.injectors;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
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
        InsnList insnList = new InsnList();
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
        insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, RenderManagerHook.class.getName().replace(".", "/"), "onRenderEntity", "(Lnet/minecraft/entity/Entity;)V", false));
        methodNode.instructions.insert(insnList);
    }

}
