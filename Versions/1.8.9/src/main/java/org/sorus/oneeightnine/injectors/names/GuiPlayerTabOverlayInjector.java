

package org.sorus.oneeightnine.injectors.names;

import net.minecraft.client.gui.GuiPlayerTabOverlay;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.sorus.client.obfuscation.ObfuscationManager;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.Modify;

@Hook("net/minecraft/client/gui/GuiPlayerTabOverlay")
public class GuiPlayerTabOverlayInjector extends Injector<GuiPlayerTabOverlay> {

    public GuiPlayerTabOverlayInjector(GuiPlayerTabOverlay that) {
        super(that);
    }



    @Modify(name = "renderPlayerlist", desc = "(ILnet/minecraft/scoreboard/Scoreboard;Lnet/minecraft/scoreboard/ScoreObjective;)V")
    public static void modifyRenderPlayerList(MethodNode methodNode) {
        String getPlayerInfoMapClass = ObfuscationManager.getClassName("net/minecraft/client/network/NetHandlerPlayClient");
        String getPlayerInfoMapMethod = ObfuscationManager.getMethodName("net/minecraft/client/network/NetHandlerPlayClient", "getPlayerInfoMap", "()Ljava/util/Collection;");
        String getPlayerInfoMapDesc = ObfuscationManager.getDesc("()Ljava/util/Collection;");
        for(AbstractInsnNode node : methodNode.instructions.toArray()) {
            if(node instanceof MethodInsnNode && ((MethodInsnNode) node).owner.equals(getPlayerInfoMapClass) && ((MethodInsnNode) node).name.equals(getPlayerInfoMapMethod) && ((MethodInsnNode) node).desc.equals(getPlayerInfoMapDesc)) {
                InsnList insnList = new InsnList();
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, GuiPlayerTabOverlayHook.class.getName().replace(".", "/"), "transformInfo", "(Ljava/util/Collection;)Ljava/util/Collection;", false));
                methodNode.instructions.insert(node, insnList);
            }
        }

    }

}
