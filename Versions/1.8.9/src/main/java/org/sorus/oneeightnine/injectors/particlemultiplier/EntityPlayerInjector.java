package org.sorus.oneeightnine.injectors.particlemultiplier;

import net.minecraft.entity.player.EntityPlayer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.sorus.client.obfuscation.ObfuscationManager;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.Modify;

@Hook("net/minecraft/entity/player/EntityPlayer")
public class EntityPlayerInjector extends Injector<EntityPlayer> {

    public EntityPlayerInjector(EntityPlayer that) {
        super(that);
    }

    @Modify(name = "attackTargetEntityWithCurrentItem", desc = "(Lnet/minecraft/entity/Entity;)V")
    public static void modifyAttackTargetEntityWithCurrentItem(MethodNode methodNode) {
        String onCriticalHitMethod = ObfuscationManager.getMethodName("net/minecraft/entity/player/EntityPlayer", "onCriticalHit", "(Lnet/minecraft/entity/Entity;)V");
        String onEnchantmentCriticalMethod = ObfuscationManager.getMethodName("net/minecraft/entity/player/EntityPlayer", "onEnchantmentCritical", "(Lnet/minecraft/entity/Entity;)V");
        boolean foundFConst = false;
        int varThing = -1;
        int criticalVar = -1;
        for(AbstractInsnNode node : methodNode.instructions.toArray()) {
            if(node.getOpcode() == Opcodes.FCONST_0 && !foundFConst) {
                varThing = ((VarInsnNode) node.getNext()).var;
                foundFConst = true;
            }
            if(node.getOpcode() == Opcodes.ISTORE && node.getPrevious() instanceof FrameNode) {
                criticalVar = ((VarInsnNode) node).var;
            }
            if(node instanceof MethodInsnNode && ((MethodInsnNode) node).name.equals(onCriticalHitMethod)) {
                AbstractInsnNode previous = node.getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious();
                InsnList insnList = new InsnList();
                insnList.add(new VarInsnNode(Opcodes.ILOAD, criticalVar));
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, EntityPlayerHook.class.getName().replace(".", "/"), "showCriticals", "(Z)Z", false));
                insnList.add(new VarInsnNode(Opcodes.ISTORE, criticalVar));
                methodNode.instructions.insertBefore(previous, insnList);
            }
            if(node instanceof MethodInsnNode && ((MethodInsnNode) node).name.equals(onEnchantmentCriticalMethod)) {
                AbstractInsnNode previous = node.getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious().getPrevious();
                InsnList insnList = new InsnList();
                insnList.add(new VarInsnNode(Opcodes.FLOAD, varThing));
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, EntityPlayerHook.class.getName().replace(".", "/"), "getValue", "(F)F", false));
                insnList.add(new VarInsnNode(Opcodes.FSTORE, varThing));
                methodNode.instructions.insertBefore(previous, insnList);
            }
        }
    }

}
