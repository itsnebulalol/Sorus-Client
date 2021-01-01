

package org.sorus.oneeightnine.injectors.names;

import net.minecraft.client.gui.GuiNewChat;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.sorus.client.obfuscation.ObfuscationManager;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.Modify;

@Hook("net/minecraft/client/gui/GuiNewChat")
public class GuiNewChatInjector extends Injector<GuiNewChat> {

    public GuiNewChatInjector(GuiNewChat that) {
        super(that);
    }

    @Modify(name = "drawChat", desc = "(I)V")
    public static void modifyDrawChat(MethodNode methodNode) {
        String getFormattedTextClass = ObfuscationManager.getClassName("net/minecraft/util/IChatComponent");
        String getFormattedTextMethod = ObfuscationManager.getMethodName("net/minecraft/util/IChatComponent", "getFormattedText", "()Ljava/lang/String;");
        for(AbstractInsnNode node : methodNode.instructions.toArray()) {
            if(node instanceof MethodInsnNode && ((MethodInsnNode) node).owner.equals(getFormattedTextClass) && ((MethodInsnNode) node).name.equals(getFormattedTextMethod)) {
                int var = ((VarInsnNode) node.getNext()).var;
                InsnList insnList = new InsnList();
                insnList.add(new VarInsnNode(Opcodes.ALOAD, var));
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, GuiNewChatHook.class.getName().replace(".", "/"), "formatChatMessage", "(Ljava/lang/String;)Ljava/lang/String;", false));
                insnList.add(new VarInsnNode(Opcodes.ASTORE, var));
                methodNode.instructions.insert(node.getNext(), insnList);
            }
        }
    }

}
