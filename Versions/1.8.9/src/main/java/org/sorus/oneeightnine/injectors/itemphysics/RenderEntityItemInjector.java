

package org.sorus.oneeightnine.injectors.itemphysics;

import net.minecraft.client.renderer.entity.RenderEntityItem;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.sorus.client.obfuscation.ObfuscationManager;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.Modify;

@Hook("net/minecraft/client/renderer/entity/RenderEntityItem")
public class RenderEntityItemInjector extends Injector<RenderEntityItem> {

    private static boolean check = false;
    private static boolean done = false;

    public RenderEntityItemInjector(RenderEntityItem that) {
        super(that);
    }

    @Modify(name = "func_177077_a", desc = "(Lnet/minecraft/entity/item/EntityItem;DDDFLnet/minecraft/client/resources/model/IBakedModel;)I")
    public static void transformDoRender(MethodNode methodNode) {
        AbstractInsnNode node = null;
        String str = ObfuscationManager.getMethodName("net/minecraft/client/renderer/GlStateManager", "rotate", "(FFFF)V");
        for(AbstractInsnNode abstractInsnNode : methodNode.instructions.toArray()) {
            if(!done) {
                if(!check) {
                    if(abstractInsnNode instanceof VarInsnNode && abstractInsnNode.getOpcode() == Opcodes.DLOAD && ((VarInsnNode) abstractInsnNode).var == 2) {
                        check = true;
                        node = abstractInsnNode.getPrevious();
                    }
                } else {
                    if(abstractInsnNode instanceof MethodInsnNode && ((MethodInsnNode) abstractInsnNode).name.equals(str) && ((MethodInsnNode) abstractInsnNode).desc.equals("(FFFF)V")) {
                        done = true;
                    }
                    methodNode.instructions.remove(abstractInsnNode);
                }
            }
        }
        InsnList insnList = new InsnList();
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
        insnList.add(new VarInsnNode(Opcodes.DLOAD, 2));
        insnList.add(new VarInsnNode(Opcodes.DLOAD, 4));
        insnList.add(new VarInsnNode(Opcodes.FLOAD, 16));
        insnList.add(new VarInsnNode(Opcodes.DLOAD, 6));
        insnList.add(new VarInsnNode(Opcodes.FLOAD, 15));
        insnList.add(new VarInsnNode(Opcodes.FLOAD, 8));
        insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "org/sorus/oneeightnine/injectors/itemphysics/RenderEntityItemHook", "performTranslations", "(Ljava/lang/Object;DDFDFF)V", false));
        methodNode.instructions.insertBefore(node, insnList);
    }

}
