

package org.sorus.oneseventen.injectors.names;

import net.minecraft.client.renderer.entity.RendererLivingEntity;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.sorus.client.obfuscation.ObfuscationManager;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.Modify;

@Hook("net/minecraft/client/renderer/entity/RendererLivingEntity")
public class RendererLivingEntityInjector extends Injector<RendererLivingEntity> {

    public RendererLivingEntityInjector(RendererLivingEntity that) {
        super(that);
    }

    @Modify(name = "passSpecialRender", desc = "(Lnet/minecraft/entity/EntityLivingBase;DDD)V")
    public static void modifyRenderName(MethodNode methodNode) {
        String getFormattedTextClass = ObfuscationManager.getClassName("net/minecraft/util/IChatComponent");
        String getFormattedTextMethod = ObfuscationManager.getMethodName("net/minecraft/util/IChatComponent", "getFormattedText", "()Ljava/lang/String;");
        for(AbstractInsnNode node : methodNode.instructions.toArray()) {
            if(node instanceof MethodInsnNode && ((MethodInsnNode) node).owner.equals(getFormattedTextClass) && ((MethodInsnNode) node).name.equals(getFormattedTextMethod)) {
                int var = ((VarInsnNode) node.getNext()).var;
                InsnList insnList = new InsnList();
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
                insnList.add(new TypeInsnNode(Opcodes.INSTANCEOF, ObfuscationManager.getClassName("net/minecraft/client/entity/EntityPlayerSP")));
                LabelNode label = new LabelNode();
                insnList.add(new JumpInsnNode(Opcodes.IFEQ, label));
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
                insnList.add(new TypeInsnNode(Opcodes.CHECKCAST, ObfuscationManager.getClassName("net/minecraft/client/entity/EntityPlayerSP")));
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, RendererLivingEntityHook.class.getName().replace(".", "/"), "getName", "(L" + ObfuscationManager.getClassName("net/minecraft/client/entity/EntityPlayerSP") + ";)Ljava/lang/String;", false));
                insnList.add(new VarInsnNode(Opcodes.ASTORE, var));
                insnList.add(label);
                methodNode.instructions.insert(node.getNext(), insnList);
            }
        }
    }

    @Modify(name = "canRenderName", desc = "(Lnet/minecraft/entity/EntityLivingBase;)Z")
    public static void modifyCanRenderName(MethodNode methodNode) {
        InsnList insnList = new InsnList();
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
        insnList.add(new TypeInsnNode(Opcodes.INSTANCEOF, ObfuscationManager.getClassName("net/minecraft/client/entity/EntityPlayerSP")));
        LabelNode label = new LabelNode();
        insnList.add(new JumpInsnNode(Opcodes.IFEQ, label));
        insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, RendererLivingEntityHook.class.getName().replace(".", "/"), "renderSelfName", "()Z", false));
        insnList.add(new InsnNode(Opcodes.IRETURN));
        insnList.add(label);
        methodNode.instructions.insert(insnList);
    }

}
