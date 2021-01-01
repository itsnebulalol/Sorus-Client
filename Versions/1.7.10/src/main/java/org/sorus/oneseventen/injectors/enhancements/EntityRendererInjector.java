

package org.sorus.oneseventen.injectors.enhancements;

import net.minecraft.client.renderer.EntityRenderer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.sorus.client.obfuscation.ObfuscationManager;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.Modify;

@Hook("net/minecraft/client/renderer/EntityRenderer")
public class EntityRendererInjector extends Injector<EntityRenderer> {

    public EntityRendererInjector(EntityRenderer that) {
        super(that);
    }

    @Modify(name = "updateLightmap", desc = "(F)V")
    public static void modifyUpdateLightmap(MethodNode methodNode) {
        for(AbstractInsnNode node : methodNode.instructions.toArray()) {
            String fieldName = ObfuscationManager.getFieldName("net/minecraft/client/settings/GameSettings", "gammaSetting");
            if(node instanceof FieldInsnNode && ((FieldInsnNode) node).name.equals(fieldName)) {
                int var = ((VarInsnNode) node.getNext()).var;
                InsnList insnList = new InsnList();
                insnList.add(new VarInsnNode(Opcodes.FLOAD, var));
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, EntityRendererHook.class.getName().replace(".", "/"), "getBrightness", "(F)F", false));
                insnList.add(new VarInsnNode(Opcodes.FSTORE, var));
                methodNode.instructions.insert(node.getNext(), insnList);
            }
        }
    }

}
