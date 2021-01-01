

package org.sorus.oneseventen.injectors.timechanger;

import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.Modify;

@Hook("net/minecraft/world/World")
public class WorldInjector extends Injector<World> {

    public WorldInjector(World that) {
        super(that);
    }

    @Modify(name = "getCelestialAngle", desc = "(F)F")
    public static void transformGetCelestialAngle(MethodNode methodNode) {
        int thisCount = 0;
        boolean deleting = false;
        for(AbstractInsnNode node : methodNode.instructions.toArray()) {
            if(node instanceof VarInsnNode && ((VarInsnNode) node).var == 0) {
                thisCount++;
            }
            if(thisCount == 2) {
                deleting = true;
                thisCount = 0;
            }
            AbstractInsnNode nextNode = node.getNext();
            if(deleting) {
                methodNode.instructions.remove(node);
            }
            if(node instanceof MethodInsnNode && ((MethodInsnNode) node).desc.equals("()J")) {
                deleting = false;
                methodNode.instructions.insertBefore(nextNode, new MethodInsnNode(Opcodes.INVOKESTATIC, WorldHooks.class.getCanonicalName().replace(".", "/"), "getAlteredTime", "()J", false));
            }
        }
    }

}
