package org.sorus.oneeightnine.injectors.particlemultiplier;

import net.minecraft.client.particle.EntityParticleEmitter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.Modify;

@Hook("net/minecraft/client/particle/EntityParticleEmitter")
public class EntityParticleEmitterInjector extends Injector<EntityParticleEmitter> {

    public EntityParticleEmitterInjector(EntityParticleEmitter that) {
        super(that);
    }

    @Modify(name = "onUpdate")
    public static void modifyOnUpdate(MethodNode methodNode) {
        for(AbstractInsnNode node : methodNode.instructions.toArray()) {
            if(node instanceof IntInsnNode && ((IntInsnNode) node).operand == 16) {
                AbstractInsnNode previous = node.getPrevious();
                methodNode.instructions.remove(node);
                methodNode.instructions.insert(previous, new MethodInsnNode(Opcodes.INVOKESTATIC, EntityParticleEmitterHook.class.getName().replace(".", "/"), "getParticles", "()I", false));
            }
        }
    }

}
