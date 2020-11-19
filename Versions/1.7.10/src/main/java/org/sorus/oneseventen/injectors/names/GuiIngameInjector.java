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
