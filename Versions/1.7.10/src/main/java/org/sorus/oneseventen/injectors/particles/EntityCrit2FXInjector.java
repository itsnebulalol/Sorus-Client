package org.sorus.oneseventen.injectors.particles;

import net.minecraft.client.particle.EntityCrit2FX;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.Modify;

@Hook("net/minecraft/client/particle/EntityCrit2FX")
public class EntityCrit2FXInjector extends Injector<EntityCrit2FX> {

    public EntityCrit2FXInjector(EntityCrit2FX that) {
        super(that);
    }

    @Modify(name = "onUpdate")
    public static void modifyOnUpdate(MethodNode methodNode) {
        for(AbstractInsnNode node : methodNode.instructions.toArray()) {
            if(node instanceof IntInsnNode && ((IntInsnNode) node).operand == 16) {
                AbstractInsnNode previous = node.getPrevious();
                methodNode.instructions.remove(node);
                methodNode.instructions.insert(previous, new MethodInsnNode(Opcodes.INVOKESTATIC, EntityCrit2FXHook.class.getName().replace(".", "/"), "getParticles", "()I", false));
            }
        }
    }

}
