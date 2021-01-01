

package org.sorus.oneeightnine.injectors.oldanimations;

import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.sorus.client.Sorus;
import org.sorus.client.module.impl.oldanimations.OldAnimations;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.Modify;

@Hook("net/minecraft/client/renderer/entity/layers/LayerArmorBase")
public class LayerArmorBaseInjector extends Injector<LayerArmorBase<?>> {

    public LayerArmorBaseInjector(LayerArmorBase<?> that) {
        super(that);
    }

    @Modify(name = "shouldCombineTextures", desc = "()Z")
    public static void transformShouldCombineTextures(MethodNode methodNode) {
        InsnList insnList = new InsnList();
        insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, LayerArmorBaseInjector.class.getCanonicalName().replace(".", "/"), "shouldRenderRedArmor", "()Z", false));
        insnList.add(new InsnNode(Opcodes.IRETURN));
        methodNode.instructions.insert(insnList);
    }

    public static boolean shouldRenderRedArmor() {
        return Sorus.getSorus().getModuleManager().getModule(OldAnimations.class).shouldShowRedArmor();
    }

}
