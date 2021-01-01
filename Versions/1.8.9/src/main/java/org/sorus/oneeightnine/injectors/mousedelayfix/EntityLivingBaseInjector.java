

package org.sorus.oneeightnine.injectors.mousedelayfix;

import net.minecraft.entity.EntityLivingBase;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.sorus.client.obfuscation.ObfuscationManager;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.Modify;

@Hook("net/minecraft/entity/EntityLivingBase")
public class EntityLivingBaseInjector extends Injector<EntityLivingBase> {

    public EntityLivingBaseInjector(EntityLivingBase that) {
        super(that);
    }

    @Modify(name = "getLook", desc = "(F)Lnet/minecraft/util/Vec3;")
    public static void transformGetLook(MethodNode methodNode) {
        InsnList insnList = new InsnList();
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
        insnList.add(new TypeInsnNode(Opcodes.INSTANCEOF, ObfuscationManager.getClassName("net/minecraft/client/entity/EntityPlayerSP")));
        LabelNode label = new LabelNode();
        insnList.add(new JumpInsnNode(Opcodes.IFEQ, label));
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
        insnList.add(new VarInsnNode(Opcodes.FLOAD, 1));
        insnList.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, ObfuscationManager.getClassName("net/minecraft/entity/Entity"), methodNode.name, methodNode.desc, false));
        insnList.add(new InsnNode(Opcodes.ARETURN));
        insnList.add(label);
        methodNode.instructions.insert(insnList);
    }

}
