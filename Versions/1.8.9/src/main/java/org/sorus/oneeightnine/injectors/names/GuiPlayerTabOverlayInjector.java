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

    /*@Modify(name = "renderPlayerlist", desc = "(ILnet/minecraft/scoreboard/Scoreboard;Lnet/minecraft/scoreboard/ScoreObjective;)V")
    public static void modifyRenderPlayerList(MethodNode methodNode) {
        String getPlayerNameClass = ObfuscationManager.getClassName("net/minecraft/client/gui/GuiPlayerTabOverlay");
        String getPlayerNameMethod = ObfuscationManager.getMethodName("net/minecraft/client/gui/GuiPlayerTabOverlay", "getPlayerName", "(Lnet/minecraft/client/network/NetworkPlayerInfo;)Ljava/lang/String;");
        String getPlayerNameDesc = ObfuscationManager.getDesc("(Lnet/minecraft/client/network/NetworkPlayerInfo;)Ljava/lang/String;");
        for(AbstractInsnNode node : methodNode.instructions.toArray()) {
            if(node instanceof MethodInsnNode && ((MethodInsnNode) node).owner.equals(getPlayerNameClass) && ((MethodInsnNode) node).name.equals(getPlayerNameMethod) && ((MethodInsnNode) node).desc.equals(getPlayerNameDesc)) {
                if(node.getNext().getNext().getNext().getNext() instanceof VarInsnNode) {
                    int var = ((VarInsnNode) node.getNext()).var;
                    InsnList insnList = new InsnList();
                    insnList.add(new VarInsnNode(Opcodes.ALOAD, var - 1));
                    insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, GuiPlayerTabOverlayHook.class.getName().replace(".", "/"), "getName", "(L" + ObfuscationManager.getClassName("net/minecraft/client/network/NetworkPlayerInfo") + ";)Ljava/lang/String;", false));
                    insnList.add(new VarInsnNode(Opcodes.ASTORE, var));
                    methodNode.instructions.insert(node.getNext(), insnList);
                }
            }
        }
    }*/

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
        /*String getPlayerNameClass = ObfuscationManager.getClassName("net/minecraft/client/gui/GuiPlayerTabOverlay");
        String getPlayerNameMethod = ObfuscationManager.getMethodName("net/minecraft/client/gui/GuiPlayerTabOverlay", "getPlayerName", "(Lnet/minecraft/client/network/NetworkPlayerInfo;)Ljava/lang/String;");
        String getPlayerNameDesc = ObfuscationManager.getDesc("(Lnet/minecraft/client/network/NetworkPlayerInfo;)Ljava/lang/String;");
        for(AbstractInsnNode node : methodNode.instructions.toArray()) {
            if(node instanceof MethodInsnNode && ((MethodInsnNode) node).owner.equals(getPlayerNameClass) && ((MethodInsnNode) node).name.equals(getPlayerNameMethod) && ((MethodInsnNode) node).desc.equals(getPlayerNameDesc)) {
                if(node.getNext().getNext().getNext().getNext() instanceof VarInsnNode) {
                    int var = ((VarInsnNode) node.getNext()).var;
                    InsnList insnList = new InsnList();
                    insnList.add(new VarInsnNode(Opcodes.ALOAD, var - 1));
                    insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, GuiPlayerTabOverlayHook.class.getName().replace(".", "/"), "getName", "(L" + ObfuscationManager.getClassName("net/minecraft/client/network/NetworkPlayerInfo") + ";)Ljava/lang/String;", false));
                    insnList.add(new VarInsnNode(Opcodes.ASTORE, var));
                    methodNode.instructions.insert(node.getNext(), insnList);
                }
            }
        }*/
    }

}
