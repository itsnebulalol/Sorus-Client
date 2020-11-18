/*
 * MIT License
 *
 * Copyright (c) 2020 Danterus
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
