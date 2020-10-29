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

package org.sorus.oneeightnine.injectors.enhancements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.sorus.client.Sorus;
import org.sorus.client.module.impl.enhancements.Enhancements;
import org.sorus.client.startup.injection.Hook;
import org.sorus.client.startup.injection.Injector;
import org.sorus.client.startup.injection.Modify;

@Hook("net/minecraft/client/renderer/InventoryEffectRenderer")
public class InventoryEffectRendererInjector extends Injector<InventoryEffectRenderer> {

    public InventoryEffectRendererInjector(InventoryEffectRenderer that) {
        super(that);
    }

    @Modify(name = "updateActivePotionEffects")
    public static void modifyUpdateActivePotionEffects(MethodNode methodNode) {
        LabelNode labelNode = new LabelNode();
        for(AbstractInsnNode node : methodNode.instructions.toArray()) {
            if(node instanceof VarInsnNode && node.getNext() instanceof IntInsnNode) {
                InsnList insnList = new InsnList();
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, InventoryEffectRendererInjector.class.getCanonicalName().replace(".", "/"), "shouldShiftInventory", "()Z", false));
                insnList.add(new JumpInsnNode(Opcodes.IFEQ, labelNode));
                methodNode.instructions.insertBefore(node, insnList);
            }
            if(node instanceof LineNumberNode && node.getNext() instanceof VarInsnNode && node.getNext().getNext() instanceof InsnNode && node.getNext().getNext().getOpcode() == 4) {
                methodNode.instructions.insertBefore(node, labelNode);
            }
        }
    }

    public static boolean shouldShiftInventory() {
        Enhancements enhancements = Sorus.getSorus().getModuleManager().getModule(Enhancements.class);
        return enhancements.isEnabled() && !enhancements.shouldCenterInventory();
    }

}
