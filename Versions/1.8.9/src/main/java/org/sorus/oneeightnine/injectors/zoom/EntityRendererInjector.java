package org.sorus.oneeightnine.injectors.zoom;

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

    @Modify(name = "getFOVModifier", desc = "(FZ)F")
    public static void modifyGetFOVModifier(MethodNode methodNode) {
        String fovSettingOwner = ObfuscationManager.getClassName("net/minecraft/client/settings/GameSettings");
        String fovSettingName = ObfuscationManager.getFieldName("net/minecraft/client/settings/GameSettings", "fovSetting");
        for(AbstractInsnNode node : methodNode.instructions.toArray()) {
            if(node instanceof FieldInsnNode && ((FieldInsnNode) node).owner.equals(fovSettingOwner) && ((FieldInsnNode) node).name.equals(fovSettingName)) {
                int var = ((VarInsnNode) node.getNext()).var;
                InsnList insnList = new InsnList();
                insnList.add(new VarInsnNode(Opcodes.FLOAD, var));
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, EntityRendererHook.class.getName().replace(".", "/"), "getFOV", "(F)F", false));
                insnList.add(new VarInsnNode(Opcodes.FSTORE, var));
                methodNode.instructions.insert(node.getNext(), insnList);
            }
        }
    }

}
