

package org.sorus.oneseventen.injectors.names;

import net.minecraft.client.gui.GuiIngame;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.sorus.client.obfuscation.ObfuscationManager;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.Modify;

@Hook("net/minecraft/client/gui/GuiIngame")
public class GuiIngameInjector extends Injector<GuiIngame> {

    public GuiIngameInjector(GuiIngame that) {
        super(that);
    }

    @Modify(name = "renderGameOverlay", desc = "(FZII)V")
    public static void modifyRenderGameOverlay(MethodNode methodNode) {
        String nameClass = ObfuscationManager.getClassName("net/minecraft/client/gui/GuiPlayerInfo");
        String nameField = ObfuscationManager.getFieldName("net/minecraft/client/gui/GuiPlayerInfo", "name");
        String formatPlayerNameClass = ObfuscationManager.getClassName("net/minecraft/scoreboard/ScorePlayerTeam");
        String formatPlayerNameMethod = ObfuscationManager.getMethodName("net/minecraft/scoreboard/ScorePlayerTeam", "formatPlayerName", "(Lnet/minecraft/scoreboard/Team;Ljava/lang/String;)Ljava/lang/String;");
        for(AbstractInsnNode node : methodNode.instructions.toArray()) {
            if(node instanceof FieldInsnNode && ((FieldInsnNode) node).owner.equals(nameClass) && ((FieldInsnNode) node).name.equals(nameField)) {
                if(node.getNext() instanceof MethodInsnNode && ((MethodInsnNode) node.getNext()).owner.equals(formatPlayerNameClass) && ((MethodInsnNode) node.getNext()).name.equals(formatPlayerNameMethod)) {
                    InsnList insnList = new InsnList();
                    insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, GuiIngameHook.class.getName().replace(".", "/"), "getName", "(Ljava/lang/String;)Ljava/lang/String;", false));
                    methodNode.instructions.insert(node, insnList);
                }
            }
        }
    }

}
